package com.wuxp.codegen.core.parser;


import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaBaseMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.ResolvableType;
import org.springframework.util.StringUtils;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * 解析一个java class对象
 */
@Slf4j
public class JavaClassParser implements GenericParser<JavaClassMeta, Class<?>> {


    private final static Map<Class<?>, JavaClassMeta> PARSER_CACHE = new ConcurrentHashMap<>();


    //spring 解析泛型是标记为空类型类名称
    protected static final String EMPTY_TYPE_NAME = "ResolvableType$EmptyType";

    //spring的方法参数发现者
    protected ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    protected boolean onlyPublic;

    public JavaClassParser(boolean onlyPublic) {
        this.onlyPublic = onlyPublic;
    }

    @Override
    public JavaClassMeta parse(Class<?> source) {

        if (source == null) {
            return null;
        }
        if (PARSER_CACHE.containsKey(source)) {
            return PARSER_CACHE.get(source);
        }

        JavaClassMeta classMeta = new JavaClassMeta();
        if (source.isInterface()) {
            //接口
            classMeta.setClassType(ClassType.INTERFACE);
        } else if (source.isEnum()) {
            //枚举
            classMeta.setClassType(ClassType.ENUM);
        } else if (source.isAnnotation()) {
            classMeta.setClassType(ClassType.ANNOTATION);
        } else {
            classMeta.setClassType(ClassType.CLASS);
        }

        int modifiers = source.getModifiers();

        Map<Class<?>, Class<?>[]> superTypeVariables = new LinkedHashMap<>();

        //超类
        ResolvableType superType = ResolvableType.forClass(source).getSuperType();

        //循环获取超类
        while (superType.getType() != null && !superType.getType().getTypeName().contains(EMPTY_TYPE_NAME)) {
            Type subType = superType.getSuperType().getType();

            if (log.isDebugEnabled()) {
                log.debug("查找类 {} 的超类", subType.getTypeName());
            }


            ResolvableType[] superTypeGenerics = superType.getGenerics();
            List<Class<?>> list = Arrays.stream(superTypeGenerics).map((type) -> {
                Class<?> rawClass = type.getRawClass();
                if (rawClass == null) {
                    Type typeType = type.getType();
                    if (typeType instanceof Class) {
                        return (Class<?>) typeType;
                    } else {
                        return null;
                    }
                }


                return rawClass;
            }).filter(Objects::nonNull)
                    .collect(Collectors.toList());

            superTypeVariables.put(superType.getRawClass(), list.toArray(new Class<?>[]{}));
            superType = superType.getSuperType();

            if (Object.class.equals(subType)) {
                break;
            }
        }


        TypeVariable<? extends Class<?>>[] typeParameters = source.getTypeParameters();

        getAssessPermission(modifiers, classMeta);

        classMeta.setClassName(source.getName())
                .setClazz(source)
                .setIsAbstract(Modifier.isAbstract(modifiers))
                .setSuperTypeVariables(superTypeVariables)
                .setMethodMetas(this.getMethods(source, onlyPublic))
                .setFieldMetas(this.getFields(source, onlyPublic))
                .setInterfaces(source.getInterfaces())
                .setDependencyList(this.fetchDependencies(source, classMeta.getFieldMetas(), classMeta.getMethodMetas()))
                .setSuperClass(source.getSuperclass())
                .setAnnotations(source.getAnnotations())
                .setTypeVariables(typeParameters)
                .setTypeVariableNum(typeParameters.length)
                .setName(source.getSimpleName());

        PARSER_CACHE.put(source, classMeta);

        return classMeta;
    }


    /**
     * 获取单个方法的元数据
     *
     * @param method
     * @param owner  可以为空
     * @return
     */
    protected JavaMethodMeta getJavaMethodMeta(Method method, Class<?> owner) {

        JavaMethodMeta methodMeta = new JavaMethodMeta();
        methodMeta.setMethod(method);
        //返回值
        ResolvableType returnType = ResolvableType.forMethodReturnType(method);
        methodMeta.setReturnType(genericsToClassType(returnType));

        //方法参数列表
        Map<String, Class<?>[]> params = new LinkedHashMap<String, Class<?>[]>();
        Map<String/*参数名称*/, Annotation[]> paramAnnotations = new LinkedHashMap<String/*参数名称*/, Annotation[]>();


        String[] parameterNames = null;

        try {
            //参数名称列表
            parameterNames = parameterNameDiscoverer.getParameterNames(method);
        } catch (Exception e) {
            log.error("获取参数名称列表异常", e);

        }
        if (parameterNames == null) {

            parameterNames = new String[0];
        }
        //参数列表
        Parameter[] parameters = method.getParameters();
        if (parameters.length != parameterNames.length) {
            //参数个数不匹配
            log.error("获取参数名称列表失败");
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String parameterName = parameter.getName();
            if (parameterName.startsWith("arg")) {
                //重新获取
                parameterName = parameterNames[i];
            }
            if (!StringUtils.hasText(parameterName)) {
                log.error("获取方法{}的第{}个参数名称失败", method.getName(), i);
                continue;
            }
            Type type = parameter.getParameterizedType();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                List<Type> classes = new ArrayList<>();
                classes.add(parameterizedType.getRawType());
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                classes.addAll(Arrays.stream(actualTypeArguments)
                        .filter((t) -> t instanceof Class<?>)
                        .collect(Collectors.toList()));
                params.put(parameterName, classes.toArray(new Class<?>[]{}));

            } else if (type instanceof Class<?>) {
                params.put(parameterName, new Class[]{(Class<?>) type});
            } else if (type instanceof TypeVariableImpl) {
                TypeVariableImpl typeVariable = (TypeVariableImpl) type;
                params.put(parameterName, Arrays.stream(typeVariable.getBounds()).filter(b -> b instanceof Class<?>).toArray(Class<?>[]::new));
            } else {
                throw new RuntimeException("未处理的类型参数类型:" + type.getTypeName());
            }
            paramAnnotations.put(parameterName, parameter.getAnnotations());

        }


        int modifiers = method.getModifiers();
        this.getAssessPermission(modifiers, methodMeta);
        TypeVariable<Method>[] typeParameters = method.getTypeParameters();
        methodMeta.setParams(params)
                .setIsAbstract(Modifier.isAbstract(modifiers))
                .setIsSynchronized(Modifier.isSynchronized(modifiers))
                .setParamAnnotations(paramAnnotations)
                .setOwner(owner)
                .setIsNative(Modifier.isNative(modifiers))
                .setTypeVariables(typeParameters)
                .setTypeVariableNum(typeParameters.length)
                .setAnnotations(method.getAnnotations())
                .setName(method.getName());
        return methodMeta;
    }

    /**
     * 获取属性列表
     *
     * @param clazz
     * @param onlyPublic
     * @return
     */
    protected JavaFieldMeta[] getFields(Class<?> clazz, boolean onlyPublic) {

        Field[] fields = null;
        if (onlyPublic) {
            fields = clazz.getFields();
        } else {
            fields = clazz.getDeclaredFields();
            Field.setAccessible(fields, true);
        }
        List<JavaFieldMeta> fieldMetas = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];
            JavaFieldMeta fieldMeta = getJavaFieldMeta(field, clazz);
            if (fieldMeta == null) continue;
            fieldMetas.add(fieldMeta);
        }

        return fieldMetas.stream()
//                .sorted(Comparator.comparing(CommonBaseMeta::getName))
                .toArray(JavaFieldMeta[]::new);
    }

    protected JavaFieldMeta getJavaFieldMeta(Field field, Class<?> owner) {
        String fieldName = field.getName();
        if (owner.isEnum()) {
            //枚举，忽略$VALUES
            if (fieldName.equals("$VALUES")) {
                return null;
            }
        }

        JavaFieldMeta fieldMeta = new JavaFieldMeta();
        fieldMeta.setField(field)
                .setIsEnumConstant(field.isEnumConstant());

        int modifiers = field.getModifiers();
        //设置访问权限
        this.getAssessPermission(modifiers, fieldMeta);

        fieldMeta.setTypes(genericsToClassType(ResolvableType.forField(field)))
                .setIsTransient(Modifier.isTransient(modifiers))
                .setIsVolatile(Modifier.isVolatile(modifiers));


        fieldMeta.setName(fieldName);
        Annotation[] annotations = field.getAnnotations();
        fieldMeta.setAnnotations(annotations);
        Type genericType = field.getGenericType();


        //自动类型的泛型描述
        Type[] typeVariables = null;
        if (genericType instanceof ParameterizedType) {
            typeVariables = ((ParameterizedType) genericType).getActualTypeArguments();
        } else if (genericType instanceof TypeVariable) {
            typeVariables = new Type[]{genericType};
        } else {
            typeVariables = field.getType().getTypeParameters();
        }

        if (typeVariables != null) {
            fieldMeta.setTypeVariableNum(typeVariables.length);
        }

        if (typeVariables != null) {
            //判断类上面是否存在这个泛型描述的变量
            List<String> clazzTypeVariables = Arrays.stream(owner.getTypeParameters())
                    .map(Type::getTypeName)
                    .collect(Collectors.toList());

            typeVariables = Arrays.stream(typeVariables)
                    .filter(type -> clazzTypeVariables.contains(type.getTypeName()))
                    .toArray(Type[]::new);
        }

        fieldMeta.setTypeVariables(typeVariables);
        return fieldMeta;
    }


    /**
     * 获取方法列表
     *
     * @param clazz
     * @param onlyPublic
     * @return
     */
    protected JavaMethodMeta[] getMethods(Class<?> clazz, boolean onlyPublic) {

        Method[] methods = null;
        if (onlyPublic) {
            //只获取public的方法
            methods = clazz.getMethods();
        } else {
            methods = clazz.getDeclaredMethods();
            Method.setAccessible(methods, true);
        }

        List<JavaMethodMeta> methodMetas = new ArrayList<>();
        for (Method method : methods) {

            JavaMethodMeta methodMeta = getJavaMethodMeta(method, clazz);

            methodMetas.add(methodMeta);

        }

        return methodMetas.stream()
                .filter(Objects::nonNull)
//                .sorted(Comparator.comparing(CommonBaseMeta::getName))
                .toArray(JavaMethodMeta[]::new);

    }

    /**
     * 获取访问权限 static final等描述
     *
     * @param modifiers
     * @param meta
     */
    protected void getAssessPermission(int modifiers, JavaBaseMeta meta) {
        if (Modifier.isPrivate(modifiers)) {
            meta.setAccessPermission(AccessPermission.PRIVATE);
        } else if (Modifier.isProtected(modifiers)) {
            meta.setAccessPermission(AccessPermission.PROTECTED);
        } else if (Modifier.isPublic(modifiers)) {
            meta.setAccessPermission(AccessPermission.PUBLIC);
        } else {
            meta.setAccessPermission(AccessPermission.DEFAULT);
        }
        meta.setIsStatic(Modifier.isStatic(modifiers));
        meta.setIsFinal(Modifier.isFinal(modifiers));
    }


    /**
     * 获取类类型及其泛型
     *
     * @param resolvableType
     * @return
     */
    protected Class<?>[] genericsToClassType(ResolvableType resolvableType) {
        ResolvableType[] generics = resolvableType.getGenerics();
        List<Class<?>> classes = new ArrayList<>();
        classes.add(resolvableType.getRawClass());

        for (int i = 0; i < generics.length; i++) {
            ResolvableType generic = generics[i];
            Class<?>[] resolveGenerics = generic.resolveGenerics();
            classes.add(generic.resolve());
            //
            if (resolveGenerics != null && resolveGenerics.length > 0) {
                classes.addAll(Arrays.asList(resolveGenerics));
            }
        }

        return classes.stream()
                .filter(Objects::nonNull)
                .toArray(Class<?>[]::new);
    }


    /**
     * 获取该类的依赖列表
     *
     * @param clazz
     * @param fieldMetas
     * @param methodMetas
     * @return
     */
    protected Set<Class<?>> fetchDependencies(Class<?> clazz, JavaFieldMeta[] fieldMetas, JavaMethodMeta[] methodMetas) {

        Set<Class<?>> classSet = new HashSet<>();

        //来自属性的依赖
        for (JavaFieldMeta fieldMeta : fieldMetas) {
            //TODO 忽略静态属性的依赖
//            if (fieldMeta.getIsStatic()) {
//                continue;
//            }
            classSet.addAll(Arrays.asList((Class<?>[]) fieldMeta.getTypes()));
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
        Arrays.stream(clazz.getInterfaces()).forEach(c -> {
            classSet.add(c);
        });


        return classSet.stream().filter(Objects::nonNull)
                .filter(c -> !c.equals(clazz))
                .collect(Collectors.toSet());
    }

}
