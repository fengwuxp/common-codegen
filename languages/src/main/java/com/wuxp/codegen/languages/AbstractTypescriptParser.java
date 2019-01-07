package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.mapping.TypescriptTypeMapping;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import com.wuxp.codegen.utils.JavaMethodNameUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 抽象的typescript parser
 */
@Slf4j
public abstract class AbstractTypescriptParser extends AbstractLanguageParser<TypescriptClassMeta, CommonCodeGenMethodMeta, TypescriptFieldMate> {


    /**
     * 映射java类和typeScript类之间的关系
     */
    protected TypeMapping<Class<?>, Collection<TypescriptClassMeta>> typescriptTypeMapping = new TypescriptTypeMapping(this);

    public AbstractTypescriptParser(PackageMapStrategy packageMapStrategy, Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, codeDetects);
    }

    @Override
    public TypescriptClassMeta parse(Class<?> source) {

        if (source == null) {
            return null;
        }


        JavaClassMeta javaClassMeta = this.javaParser.parse(source);
        if (javaClassMeta.isApiServiceClass()) {
            //要生成的服务，判断是否需要生成
            if (!this.genMatchingStrategy.isMatchClazz(javaClassMeta)) {
                //跳过
                log.warn("跳过类{}", source.getName());
                return null;
            }
        }

        TypescriptClassMeta meta = this.getResultToLocalCache(source);

        if (meta != null) {
            return meta;
        }

        //检查代码
        this.detectJavaCode(javaClassMeta);

        meta = new TypescriptClassMeta();
        meta.setName(source.getName());
        meta.setPackagePath(this.packageMapStrategy.convert(javaClassMeta.getClazz()));
        meta.setClassType(javaClassMeta.getClassType());
        meta.setAccessPermission(javaClassMeta.getAccessPermission());


        //类上的注释
        meta.setComments(this.generateComments(source.getAnnotations()).toArray(new String[]{}));

        //类上的注解
        meta.setAnnotations(this.converterAnnotations(source.getAnnotations(), source));

        if (javaClassMeta.isApiServiceClass()) {
            //spring的控制器  生成方法列表
            meta.setMethodMetas(this.converterMethodMetas(javaClassMeta.getMethodMetas(), javaClassMeta));
        } else {
            // 普通的java bean DTO  生成属性列表
            meta.setFiledMetas(this.converterFieldMetas(javaClassMeta.getFieldMetas(), javaClassMeta));
        }

        //依赖列表
        meta.setDependencies(this.fetchDependencies(javaClassMeta.getDependencyList()));

        //加入缓存列表
        HANDLE_RESULT_CACHE.put(javaClassMeta.getClazz(), meta);

        return meta;
    }

    @Override
    protected TypescriptFieldMate[] converterFieldMetas(JavaFieldMeta[] javaFieldMetas, JavaClassMeta classMeta) {

        if (javaFieldMetas == null) {
            return new TypescriptFieldMate[0];
        }


        final List<String> fieldNameList = Arrays.stream(javaFieldMetas)
                .map(CommonBaseMeta::getName)
                .collect(Collectors.toList());

        //如果是java bean 需要合并get方法
        // 找出java bean中不存在属性定义的get或 is方法
        List<TypescriptFieldMate> typescriptFieldMates = Arrays.stream(classMeta.getMethodMetas())
                .filter(javaMethodMeta -> {
                    //匹配getXX 或isXxx方法
                    return JavaMethodNameUtil.isGetMethodOrIsMethod(javaMethodMeta.getName());
                })
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()) && Boolean.FALSE.equals(javaMethodMeta.getIsAbstract()))
                .filter(javaMethodMeta -> javaMethodMeta.getReturnType() != null && javaMethodMeta.getReturnType().length > 0)
                .filter(javaMethodMeta -> {
                    //属性是否已经存在
                    return !fieldNameList.contains(JavaMethodNameUtil.replaceGetOrIsPrefix(javaMethodMeta.getName()));
                })
                .map(javaMethodMeta -> {
                    //从get方法或is方法中生成field
                    TypescriptFieldMate typescriptFieldMate = new TypescriptFieldMate();

                    typescriptFieldMate.setName(JavaMethodNameUtil.replaceGetOrIsPrefix(javaMethodMeta.getName()));
                    typescriptFieldMate.setRequired(true);
                    typescriptFieldMate.setAccessPermission(AccessPermission.PUBLIC);
                    typescriptFieldMate.setIsFinal(false);
                    typescriptFieldMate.setIsStatic(false);
                    typescriptFieldMate.setFiledTypes(this.typescriptTypeMapping
                            .mapping(javaMethodMeta.getReturnType())
                            .toArray(new TypescriptClassMeta[]{}));
                    return typescriptFieldMate;
                }).collect(Collectors.toList());

        typescriptFieldMates.addAll(Arrays.stream(javaFieldMetas)
                .filter(javaFieldMeta -> !javaFieldMeta.getIsStatic())
                .map(javaFieldMeta -> {
                    TypescriptFieldMate typescriptFieldMate = new TypescriptFieldMate();

                    typescriptFieldMate.setName(javaFieldMeta.getName());
                    typescriptFieldMate.setAccessPermission(javaFieldMeta.getAccessPermission());

                    //注释来源于注解和java的类类型
                    List<String> comments = super.generateComments(javaFieldMeta.getAnnotations());
                    comments.addAll(super.generateComments(javaFieldMeta.getTypes()));

                    //注解
                    typescriptFieldMate.setComments(comments.toArray(new String[]{}));

                    //注解
                    typescriptFieldMate.setAnnotations(this.converterAnnotations(javaFieldMeta.getAnnotations(), javaFieldMeta));

                    //是否必填
                    typescriptFieldMate.setRequired(javaFieldMeta.existAnnotation(NotNull.class));

                    //field 类型类别
                    Collection<TypescriptClassMeta> typescriptClassMetas = this.typescriptTypeMapping.mapping(javaFieldMeta.getTypes());
                    if (typescriptClassMetas != null) {
                        //域对象类型描述
                        typescriptFieldMate.setFiledTypes(typescriptClassMetas.toArray(new TypescriptClassMeta[]{}));
                    } else {
                        //解析失败
                        throw new RuntimeException(String.format("解析类 %s 上的属性 %s 的类型 %s 失败", classMeta.getClassName(), javaFieldMeta.getName(), this.classToNamedString(javaFieldMeta.getTypes())));
                    }

                    //TODO 注解转化

                    //增强处理
                    this.enhancedProcessingField(typescriptFieldMate, javaFieldMeta, classMeta);

                    return typescriptFieldMate;
                }).collect(Collectors.toList()));


        return typescriptFieldMates.toArray(new TypescriptFieldMate[]{});
    }


    @Override
    protected CommonCodeGenMethodMeta[] converterMethodMetas(JavaMethodMeta[] javaMethodMetas, JavaClassMeta classMeta) {
        if (javaMethodMetas == null) {
            return new CommonCodeGenMethodMeta[0];
        }
        return Arrays.stream(javaMethodMetas)
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()) && Boolean.FALSE.equals(javaMethodMeta.getIsAbstract()))
                .map(javaMethodMeta -> {
                    CommonCodeGenMethodMeta genMethodMeta = new CommonCodeGenMethodMeta();
                    //method转换
                    genMethodMeta.setAccessPermission(javaMethodMeta.getAccessPermission());
                    //注解转注释
                    List<String> comments = super.generateComments(javaMethodMeta.getAnnotations());
                    comments.addAll(super.generateComments(javaMethodMeta.getReturnType()));
                    genMethodMeta.setComments(comments.toArray(new String[]{}));
                    genMethodMeta.setName(javaMethodMeta.getName());

                    //处理方法上的相关注解
                    genMethodMeta.setAnnotations(this.converterAnnotations(javaMethodMeta.getAnnotations(), javaMethodMeta));


                    //TODO support spring webflux

                    //处理返回值
                    Collection<TypescriptClassMeta> typescriptClassMetas = this.typescriptTypeMapping.mapping(javaMethodMeta.getReturnType());

                    if (typescriptClassMetas != null) {
                        //域对象类型描述
                        genMethodMeta.setReturnTypes(typescriptClassMetas.toArray(new TypescriptClassMeta[]{}));
                    } else {
                        //解析失败
                        throw new RuntimeException(String.format("解析类 %s 上的方法 %s 的返回值类型 %s 失败",
                                classMeta.getClassName(),
                                javaMethodMeta.getName(),
                                this.classToNamedString(javaMethodMeta.getReturnType())));
                    }


                    //处理方法的参数
                    //1: 参数过滤（过滤掉控制器方法中servlet相关的参数等等）
                    Map<String, Class<?>[]> methodMetaParams = javaMethodMeta.getParams();
                    //有效的参数
                    Map<String, Class<?>[]> effectiveParams = new LinkedHashMap<>();
                    methodMetaParams.forEach((key, classes) -> {
                        Class<?>[] array = Arrays.stream(classes)
                                .filter(this.filterClassByLibrary::filter)
                                .toArray(Class<?>[]::new);
                        if (array.length == 0) {
                            return;
                        }
                        effectiveParams.put(key, array);
                    });

                    //2: 合并参数列表，将参数列表中的简单类型参数和复杂的类型参数合并到一个列表中
                    //2.1：遍历展开参数列表

                    final List<TypescriptFieldMate> typescriptFieldMates = new LinkedList<TypescriptFieldMate>();
                    //参数的元数据类型信息
                    final TypescriptClassMeta argsClassMeta = new TypescriptClassMeta();
                    effectiveParams.forEach((key, classes) -> {

                        Class<?> clazz = classes[0];
                        if (JavaTypeUtil.isComplex(clazz)) {
                            //复杂的数据类型
                            JavaClassMeta javaClassMeta = this.javaParser.parse(clazz);
                            Class<?>[] interfaces = javaClassMeta.getInterfaces();
                            //转换
                            argsClassMeta.setName(clazz.getSimpleName());
                            argsClassMeta.setPackagePath(clazz.getPackage().getName());
                            argsClassMeta.setAccessPermission(AccessPermission.PUBLIC);
                            argsClassMeta.setInterfaces(Arrays.stream(interfaces).map(this::parseSupper).toArray(TypescriptClassMeta[]::new));
                            argsClassMeta.setSuperClass(this.parseSupper(javaClassMeta.getSuperClass()));
                            TypescriptFieldMate[] fields = this.converterFieldMetas(javaClassMeta.getFieldMetas(), javaClassMeta);
                            typescriptFieldMates.addAll(Arrays.stream(fields).collect(Collectors.toList()));

                        } else if (clazz.isEnum()) {
                            //枚举
                            TypescriptFieldMate fieldMate = new TypescriptFieldMate();

                            typescriptFieldMates.add(fieldMate);

                        } else if (clazz.isArray()) {
                            //TODO　数组

                        } else if (JavaTypeUtil.isCollection(clazz)) {
                            //TODO 集合
                        } else if (JavaTypeUtil.isSet(clazz)) {
                            //TODO set
                        } else if (JavaTypeUtil.isMap(clazz)) {
                            //TODO map
                        } else if (JavaTypeUtil.isJavaBaseType(clazz)) {
                            //简单数据类型
                            Collection<TypescriptClassMeta> classMetas = this.typescriptTypeMapping.mapping(classes);
                            TypescriptFieldMate typescriptFieldMate = new TypescriptFieldMate();
                            typescriptFieldMate.setName(key);

                            Annotation[] annotations = javaMethodMeta.getParamAnnotations().get(key);

                            //TODO 参数是否必须 是否为控制器  是否存在javax的验证注解、或者spring mvc相关注解 required=true 或者是swagger注解
//                    Arrays.stream(annotations).filter(annotation -> {
//                        annotation.annotationType().equals(Reque)
//                        return true;
//                    })

//                    typescriptFieldMate.setRequired();
                            typescriptFieldMates.add(typescriptFieldMate);
                        } else {
                            log.warn("未处理的类型{}", clazz.getName());
                        }


                    });

                    //3: 重组，使用第二部得到的列表构建一个信息的 TypescriptClassMeta对象，类型为typescript的interface

                    argsClassMeta.setClassType(ClassType.INTERFACE);
                    argsClassMeta.setFiledMetas(typescriptFieldMates.toArray(new TypescriptFieldMate[]{}));
                    LinkedHashMap<String, CommonCodeGenClassMeta> params = new LinkedHashMap<>();
                    //请求参数名称，固定为req
                    params.put("req", argsClassMeta);
                    genMethodMeta.setParams(params);
                    //增强处理
                    this.enhancedProcessingMethod(genMethodMeta, javaMethodMeta, classMeta);

                    return genMethodMeta;
                }).toArray(CommonCodeGenMethodMeta[]::new);
    }

}
