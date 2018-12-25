package com.wuxp.codegen.core.parser;


import com.wuxp.codegen.model.languages.java.JavaBaseMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.enums.ClassType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.ResolvableType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 解析一个java class对象
 */
@Slf4j
public class JavaClassParser implements GenericParser<JavaClassMeta, Class<?>> {


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

        JavaClassMeta.JavaClassMetaBuilder javaClassMetaBuilder = JavaClassMeta.builder();

        if (source.isInterface()) {
            //接口
            javaClassMetaBuilder.classType(ClassType.INTERFACE);
        } else if (source.isEnum()) {
            //枚举
            javaClassMetaBuilder.classType(ClassType.ENUM);
        } else if (source.isAnnotation()) {
            javaClassMetaBuilder.classType(ClassType.ANNOTATION);
        } else {
            javaClassMetaBuilder.classType(ClassType.CLASS);
        }

        int modifiers = source.getModifiers();
        ResolvableType resolvableType = ResolvableType.forClass(source);

        Map<Class<?>, Class<?>[]> types = new LinkedHashMap<>();
        ResolvableType superType = resolvableType;


        //循环获取超类
        while (superType.getType() != null && !superType.getType().getTypeName().contains(EMPTY_TYPE_NAME)) {
            log.info(superType.getSuperType().getType().getTypeName());
            ResolvableType[] superTypeGenerics = superType.getGenerics();
            List<Class<?>> list = Arrays.stream(superTypeGenerics).map((type) -> {
                Class<?> rawClass = type.getRawClass();
                return rawClass == null ? (Class<?>) type.getType() : rawClass;
            }).collect(Collectors.toList());
            types.put(superType.getRawClass(), list.toArray(new Class<?>[]{}));
            superType = superType.getSuperType();
        }

        javaClassMetaBuilder.className(source.getName())
                .clazz(source)
                .isAbstract(Modifier.isAbstract(modifiers))
                .types(types)
                .methodMetas(this.getMethods(source, onlyPublic))
                .fieldMetas(this.getFields(source, onlyPublic));


        JavaClassMeta classMeta = javaClassMetaBuilder.build();
        classMeta.setDependencyList(this.fetchDependencies(source, classMeta.getFieldMetas(), classMeta.getMethodMetas()));
        getAssessPermission(modifiers, classMeta);
        classMeta.setName(source.getSimpleName());
        classMeta.setAnnotations(source.getAnnotations());

        classMeta.setInterfaces(source.getInterfaces());
        classMeta.setSuperClass(source.getSuperclass());

        return classMeta;
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
            int modifiers = field.getModifiers();
            JavaFieldMeta.JavaFieldMetaBuilder builder = JavaFieldMeta.builder();

            JavaFieldMeta fieldMeta = builder
                    .types(genericsToClassType(ResolvableType.forField(field)))
                    .isTransient(Modifier.isTransient(modifiers))
                    .isVolatile(Modifier.isVolatile(modifiers))
                    .build();
            this.getAssessPermission(modifiers, fieldMeta);
            fieldMeta.setName(field.getName());
            Annotation[] annotations = field.getAnnotations();
            fieldMeta.setAnnotations(annotations);
            fieldMetas.add(fieldMeta);
        }

        return fieldMetas.toArray(new JavaFieldMeta[]{});
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
            JavaMethodMeta.JavaMethodMetaBuilder builder = JavaMethodMeta.builder();

            //返回值
            ResolvableType returnType = ResolvableType.forMethodReturnType(method);
            builder.returnType(genericsToClassType(returnType));

            //方法参数列表
            Map<String, Class<?>[]> params = new LinkedHashMap<String, Class<?>[]>();
            Map<String/*参数名称*/, Annotation[]> paramAnnotations = new LinkedHashMap<String/*参数名称*/, Annotation[]>();

            //参数类型列表
            Class<?>[] parameterTypes = method.getParameterTypes();
            //参数上的注解
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            ;
            //参数名称列表
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);

            if (parameterTypes.length > 0 && (parameterNames != null && parameterNames.length > 0)) {
                log.info("参数名称列表：" + parameterNames.length);
                log.info("参数列表：" + parameterTypes.length);
                for (int k = 0; k < parameterTypes.length; k++) {
                    String parameterName = parameterNames[k];
                    params.put(parameterName, genericsToClassType(ResolvableType.forClass(parameterTypes[k])));
                    paramAnnotations.put(parameterName, parameterAnnotations[k]);
                }

            }


            int modifiers = method.getModifiers();
            builder.params(params)
                    .isAbstract(Modifier.isAbstract(modifiers))
                    .isSynchronized(Modifier.isSynchronized(modifiers))
                    .paramAnnotations(paramAnnotations)
                    .owner(clazz)
                    .isNative(Modifier.isNative(modifiers));

            JavaMethodMeta methodMeta = builder.build();
            this.getAssessPermission(modifiers, methodMeta);
            methodMeta.setName(method.getName());
            methodMeta.setAnnotations(method.getAnnotations());
            methodMetas.add(methodMeta);
        }

        return methodMetas.toArray(new JavaMethodMeta[]{});

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
            meta.setAccessPermission(AccessPermission.PROTECTED);
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

        return classes.toArray(new Class<?>[]{});
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


        return classSet;
    }

}
