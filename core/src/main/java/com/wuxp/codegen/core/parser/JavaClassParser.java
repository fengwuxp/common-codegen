package com.wuxp.codegen.core.parser;


import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.util.ReflectUtils;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.beans.Transient;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * 解析一个java class对象
 *
 * @author wuxp
 */
@Slf4j
public class JavaClassParser {

    public static final JavaClassParser JAVA_CLASS_ON_PUBLIC_PARSER = new JavaClassParser(true);

    public static final JavaClassParser JAVA_CLASS_PARSER = new JavaClassParser(false);

    /**
     * spring的方法参数发现者
     */
    protected static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private static final Map<Class<?>, JavaClassMeta> PARSER_CACHE = new ConcurrentHashMap<>();
    /**
     * 是否只过滤public的方法
     */
    protected final boolean onlyPublic;


    public JavaClassParser(boolean onlyPublic) {
        this.onlyPublic = onlyPublic;
    }

    /**
     * 获取参数的真实名称
     *
     * @param parameter 方法参数对象
     * @return 参数的名称
     */
    public static String getParameterName(Parameter parameter) {
        Method method = (Method) parameter.getDeclaringExecutable();
        int index = Arrays.asList(method.getParameters()).indexOf(parameter);
        try {
            return Objects.requireNonNull(PARAMETER_NAME_DISCOVERER.getParameterNames(method))[index];
        } catch (Exception e) {
            log.warn("获取方法{}的参数名称列表失败：{}", method, e.getMessage(), e);
        }
        return parameter.getName();
    }

    public JavaClassMeta parse(Class<?> source) {
        if (source == null) {
            return null;
        }
        return PARSER_CACHE.computeIfAbsent(source, this::parseClass);
    }

    private JavaClassMeta parseClass(Class<?> source) {
        JavaClassMeta classMeta = new JavaClassMeta(source.getModifiers());
        classMeta.setClassType(getClassType(source))
                .setClassName(source.getName())
                .setClazz(source)
                .setSuperTypeVariables(getSuperTypeVariables(source))
                .setMethodMetas(this.getMethodMetas(source, onlyPublic))
                .setFieldMetas(this.getFieldMetas(source, onlyPublic))
                .setInterfaces(source.getInterfaces())
                .setDependencyList(fetchDependencies(source, classMeta.getFieldMetas(), classMeta.getMethodMetas()))
                .setSuperClass(source.getSuperclass())
                .setTypeVariables(source.getTypeParameters())
                .setAnnotations(source.getAnnotations())
                .setName(source.getSimpleName());
        return classMeta;
    }

    private Map<Class<?>, Class<?>[]> getSuperTypeVariables(Class<?> clazz) {
        Map<Class<?>, Class<?>[]> superTypeVariables = new LinkedHashMap<>();
        getSuperTypeGenericTypeVariables(clazz).forEach((key, val) -> {
            Class<?>[] classes = Arrays.stream(val)
                    .map(ClassGenericVariableDesc::getType)
                    .toArray(Class[]::new);
            superTypeVariables.put(key, classes);
        });
        return superTypeVariables;
    }


    public JavaMethodMeta getJavaMethodMeta(Method method) {
        JavaMethodMeta methodMeta = new JavaMethodMeta(method.getModifiers());
        methodMeta.setMethod(method)
                .setReturnType(getGenericsAndClassType(ResolvableType.forMethodReturnType(method)))
                .setParams(getParamTypes(method))
                .setParameters(getMethodParameters(method))
                .setIsTransient(Modifier.isTransient(method.getModifiers()) || method.isAnnotationPresent(Transient.class))
                .setParamAnnotations(getParameterAnnotations(method))
                .setOwner(method.getDeclaringClass())
                .setTypeVariables(method.getTypeParameters())
                .setAnnotations(method.getAnnotations())
                .setName(method.getName());
        return methodMeta;
    }

    private ClassType getClassType(Class<?> source) {
        if (source.isInterface()) {
            //接口
            return ClassType.INTERFACE;
        } else if (source.isEnum()) {
            //枚举
            return ClassType.ENUM;
        } else if (source.isAnnotation()) {
            return ClassType.ANNOTATION;
        } else {
            return ClassType.CLASS;
        }
    }

    /**
     * 获取超类上的泛型变量
     *
     * @param source 类对象
     * @return 类的超类泛型变量描述
     */
    private Map<Class<?>, ClassGenericVariableDesc[]> getSuperTypeGenericTypeVariables(Class<?> source) {
        Map<Class<?>, ClassGenericVariableDesc[]> superTypeVariables = new LinkedHashMap<>();
        List<ResolvableType> superTypes = new ArrayList<>();
        superTypes.add(ResolvableType.forClass(source).getSuperType());
        superTypes.addAll(Arrays.asList(ResolvableType.forClass(source).getInterfaces()));
        superTypes.forEach(superType -> {
            //循环获取超类(包括接口)
            while (isValidResolvableType(superType)) {
                // 超类上的泛型变量名称
                superTypeVariables.put(superType.getRawClass(), getClassGenericVariableDescArray(superType));
                superType = superType.getSuperType();
            }
        });
        return superTypeVariables;
    }

    private boolean isValidResolvableType(ResolvableType superType) {
        if (superType == null || ResolvableType.NONE == superType) {
            return false;
        }
        // 直到获取到 Object 为止
        return !Object.class.equals(superType.getType());
    }

    private ClassGenericVariableDesc[] getClassGenericVariableDescArray(ResolvableType superType) {
        List<String> genericTypeNames = getTypeVariableNames(superType);
        // 泛型描述
        ResolvableType[] superTypeGenerics = superType.getGenerics();
        return Arrays.stream(superTypeGenerics)
                .map(genericType -> converterGenericTypeVariableDesc(genericTypeNames.remove(0), genericType))
                .filter(Objects::nonNull)
                .toArray(ClassGenericVariableDesc[]::new);
    }

    private ClassGenericVariableDesc converterGenericTypeVariableDesc(String genericTypeName, ResolvableType genericType) {
        ClassGenericVariableDesc genericVariableDesc = new ClassGenericVariableDesc();
        Class<?> rawClass = genericType.getRawClass();
        if (rawClass == null) {
            Type typeType = genericType.getType();
            if (typeType instanceof Class) {
                genericVariableDesc.setType((Class<?>) typeType);
                genericVariableDesc.setName(genericTypeName);
            } else {
                return null;
            }
        } else {
            genericVariableDesc.setType(rawClass);
            genericVariableDesc.setName(genericTypeName);
        }
        return genericVariableDesc;
    }

    private List<String> getTypeVariableNames(ResolvableType type) {
        // 超类上的泛型变量名称
        Class<?> superTypeRawClass = type.getRawClass();
        if (superTypeRawClass == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(superTypeRawClass.getTypeParameters())
                .map(TypeVariable::getName)
                .collect(Collectors.toList());
    }

    private Map<String, Class<?>[]> getParamTypes(Method method) {
        // 方法参数列表
        Map<String, Class<?>[]> parameterTypes = new LinkedHashMap<>();
        String[] parameterNames = getParameterNames(method);
        // 参数列表
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameterNames.length; i++) {
            String parameterName = parameterNames[i];
            parameterTypes.put(parameterName, getParameterTypes(parameters[i]));
        }
        return parameterTypes;
    }

    private Class<?>[] getParameterTypes(Parameter parameter) {
        String typeName = parameter.getParameterizedType().getTypeName();
        Method method = (Method) parameter.getDeclaringExecutable();
        Class<?> owner = method.getDeclaringClass();
        List<String> ownerTypeParameterNames = getOwnerTypeParameterNames(owner);
        Map<Class<?>, ClassGenericVariableDesc[]> superTypeVariables = this.getSuperTypeGenericTypeVariables(owner.getSuperclass());
        if (ownerTypeParameterNames.contains(typeName)) {
            // 使用了泛型变量做参数类型
            ClassGenericVariableDesc[] variableDescList = superTypeVariables.get(owner);
            if (variableDescList == null) {
                return new Class<?>[0];
            }
            Class<?> parameterType = Arrays.stream(variableDescList)
                    .filter(typeVariable -> typeName.endsWith(typeVariable.getName()))
                    .map(ClassGenericVariableDesc::getType)
                    .findFirst()
                    .orElseThrow(() -> new CodegenRuntimeException(MessageFormat.format("获取方法中泛型类型的参数失败，类：{0}，方法:{1}",
                            owner.getName(), method.getName())));
            return new Class[]{parameterType};
        } else {
            ResolvableType parameterResolvableType = ResolvableType.forMethodParameter(method, Arrays.asList(method.getParameters()).indexOf(parameter));
            return this.getGenericsAndClassType(parameterResolvableType);
        }
    }

    private List<String> getOwnerTypeParameterNames(Class<?> owner) {
        TypeVariable<? extends Class<?>>[] ownerTypeParameters = owner.getTypeParameters();
        return Arrays.stream(ownerTypeParameters)
                .map(TypeVariable::getName)
                .collect(Collectors.toList());
    }

    private Map<String, Parameter> getMethodParameters(Method method) {
        String[] parameterNames = getParameterNames(method);
        // 参数列表
        Parameter[] parameters = method.getParameters();
        if (parameters.length != parameterNames.length) {
            // 参数个数不匹配
            log.error("获取类 {} 的 {} 方法参数名称列表失败", method.getDeclaringClass().getName(), method.getName());
            return Collections.emptyMap();
        }
        Map<String, Parameter> parameterMap = new LinkedHashMap<>(8);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String parameterName = parameter.getName();
            if (parameterName.startsWith("arg")) {
                //重新获取
                parameterName = parameterNames[i];
            }
            Assert.hasText(parameterName, MessageFormat.format("获取方法 {0} 的第 {1} 个参数名称失败", method.getName(), i));
            parameterMap.put(parameterName, parameter);
        }
        return parameterMap;
    }

    private Map<String, Annotation[]> getParameterAnnotations(Method method) {
        // 方法参数列表
        Map<String/*参数名称*/, Annotation[]> paramAnnotations = new LinkedHashMap<>();
        //参数列表
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = getParameterNames(method);
        for (int i = 0; i < parameterNames.length; i++) {
            // 获取参数的上的注解
            paramAnnotations.put(parameterNames[i], parameters[i].getAnnotations());
        }
        return paramAnnotations;
    }

    private String[] getParameterNames(Method method) {
        try {
            //参数名称列表
            String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
            if (parameterNames != null) {
                return parameterNames;
            }
        } catch (Exception exception) {
            log.error("获取参数名称列表异常,message={}", exception.getMessage(), exception);
        }
        return new String[0];
    }

    private JavaFieldMeta[] getFieldMetas(Class<?> clazz, boolean onlyPublic) {
        return Arrays.stream(getClassFields(clazz, onlyPublic))
                .filter(field -> {
                    if (clazz.isEnum()) {
                        // 枚举类型只保留枚举常量和枚举字段 过滤 $VALUES
                        return field.isEnumConstant() || !clazz.equals(field.getType().getComponentType());
                    }
                    return true;
                })
                .map(this::getJavaFieldMeta)
                .toArray(JavaFieldMeta[]::new);
    }

    private Field[] getClassFields(Class<?> clazz, boolean onlyPublic) {
        if (onlyPublic) {
            return clazz.getFields();
        } else {
            Field[] declaredFields = clazz.getDeclaredFields();
            Arrays.asList(declaredFields).forEach(ReflectionUtils::makeAccessible);
            return declaredFields;
        }
    }

    private JavaFieldMeta getJavaFieldMeta(Field field) {
        String fieldName = field.getName();
        JavaFieldMeta fieldMeta = new JavaFieldMeta(field.getModifiers());
        fieldMeta.setField(field).setIsEnumConstant(field.isEnumConstant());
        fieldMeta.setTypes(getGenericsAndClassType(ResolvableType.forField(field)));
        fieldMeta.setName(fieldName);
        fieldMeta.setAnnotations(field.getAnnotations());
        fieldMeta.setTypeVariables(getFiledTypes(field));
        return fieldMeta;
    }

    private Type[] getFiledTypes(Field field) {
        Type genericType = field.getGenericType();
        Type[] typeVariables;
        if (genericType instanceof ParameterizedType) {
            typeVariables = ((ParameterizedType) genericType).getActualTypeArguments();
        } else if (genericType instanceof TypeVariable) {
            typeVariables = new Type[]{genericType};
        } else {
            typeVariables = field.getType().getTypeParameters();
        }
        Assert.notNull(typeVariables, String.format("获取 %s 的属性 %s 的类型失败", field.getDeclaringClass().getName(), field.getName()));
        // 判断类上面是否存在这个泛型描述的变量
        List<String> clazzTypeVariables = Arrays.stream(field.getDeclaringClass().getTypeParameters())
                .map(Type::getTypeName)
                .collect(Collectors.toList());
        return Arrays.stream(typeVariables)
                .filter(type -> clazzTypeVariables.contains(type.getTypeName()))
                .toArray(Type[]::new);
    }

    /**
     * 获取方法列表
     *
     * @param clazz      当前类
     * @param onlyPublic 是否只处理公共方法
     * @return 方法列表
     */
    JavaMethodMeta[] getMethodMetas(Class<?> clazz, boolean onlyPublic) {

        Method[] methods = ReflectUtils.getDeclaredMethodsInOrder(clazz);
        if (onlyPublic) {
            //只获取public的方法
            methods = Arrays.stream(methods).filter(method -> Modifier.isPublic(method.getModifiers())).toArray(Method[]::new);
        } else {
            Arrays.asList(methods).forEach(ReflectionUtils::makeAccessible);
        }

        return Arrays.stream(methods)
                .map(this::getJavaMethodMeta)
                .filter(Objects::nonNull)
                .distinct()
                .toArray(JavaMethodMeta[]::new);

    }


    /**
     * 获取类类型及其泛型
     */
    protected Class<?>[] getGenericsAndClassType(ResolvableType resolvableType) {
        ResolvableType[] generics = resolvableType.getGenerics();
        List<Class<?>> classes = new ArrayList<>();
        if (resolvableType.isArray()) {
            // 数组
            ResolvableType componentType = resolvableType.getComponentType();
            classes.add(JavaArrayClassTypeMark.class);
            Class<?>[] componentTypes = getGenericsAndClassType(componentType);
            classes.addAll(Arrays.asList(componentTypes));
        } else {
            classes.add(resolvableType.getRawClass());
            for (ResolvableType generic : generics) {
                classes.addAll(Arrays.asList(getGenericsAndClassType(generic)));
            }
        }
        return classes.stream()
                .filter(Objects::nonNull)
                .toArray(Class<?>[]::new);
    }

    /**
     * 获取该类的依赖列表
     */
    public static Set<Class<?>> fetchClassMethodDependencies(Class<?> clazz, JavaMethodMeta[] methodMetas) {
        return fetchDependencies(clazz, new JavaFieldMeta[0], methodMetas);
    }

    /**
     * 获取该类的依赖列表
     */
    private static Set<Class<?>> fetchDependencies(Class<?> clazz, JavaFieldMeta[] fieldMetas, JavaMethodMeta[] methodMetas) {

        Set<Class<?>> classSet = new HashSet<>();

        //来自属性的依赖
        for (JavaFieldMeta fieldMeta : fieldMetas) {
            // 忽略静态属性的依赖
            if (!Boolean.TRUE.equals(fieldMeta.getIsStatic())) {
                classSet.addAll(Arrays.asList(fieldMeta.getTypes()));
            }
        }

        //方法的依赖
        for (JavaMethodMeta methodMeta : methodMetas) {
            //返回值类型
            classSet.addAll(Arrays.asList(methodMeta.getReturnType()));
            //参数类型
            for (Map.Entry<String, Class<?>[]> entry : methodMeta.getParams().entrySet()) {
                classSet.addAll(Arrays.asList(entry.getValue()));
            }
        }

        //父类依赖
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            classSet.add(superclass);
        }

        //接口依赖
        classSet.addAll(Arrays.asList(clazz.getInterfaces()));

        return classSet.stream()
                .filter(Objects::nonNull)
                .filter(c -> !c.equals(clazz))
                .map(c -> {
                    if (c.isArray()) {
                        return Arrays.asList(c, c.getComponentType());
                    }
                    return Collections.singletonList(c);
                })
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(c -> !c.equals(JavaArrayClassTypeMark.class))
                .collect(Collectors.toSet());
    }

    /**
     * 泛型描述
     */
    @Data
    static class ClassGenericVariableDesc {

        /**
         * 泛型变量名称
         */
        private String name;

        /**
         * 泛型变量类型
         */
        private Class<?> type;

    }
}
