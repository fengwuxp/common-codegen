package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.utils.ToggleCaseUtil;
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
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
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
    protected TypeMapping<Class<?>, List<TypescriptClassMeta>> typescriptTypeMapping = new TypescriptTypeMapping(this);

    public AbstractTypescriptParser(PackageMapStrategy packageMapStrategy, CodeGenMatchingStrategy genMatchingStrategy, Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects);
    }

    @Override
    public TypescriptClassMeta parse(Class<?> source) {

        if (!this.filterClassByLibrary.filter(source)) {
            return null;
        }

        if (HANDLE_COUNT.containsKey(source)) {
            synchronized (source) {
                //标记某个类被处理的次数如果超过2次，从缓存中返回
                if (HANDLE_COUNT.get(source) > 2) {
                    return this.getResultToLocalCache(source);
                } else {
                    HANDLE_COUNT.put(source, HANDLE_COUNT.get(source) + 1);
                }
            }
        } else {
            HANDLE_COUNT.put(source, 1);
        }
        Integer count = HANDLE_COUNT.get(source);


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
        meta.setSource(source);
        meta.setName(this.packageMapStrategy.convertClassName(source.getSimpleName()));
        meta.setPackagePath(this.packageMapStrategy.convert(source));
        meta.setClassType(javaClassMeta.getClassType());
        meta.setAccessPermission(javaClassMeta.getAccessPermission());
        meta.setTypeVariables(javaClassMeta.getTypeVariables());
        meta.setSuperClass(this.parse(javaClassMeta.getSuperClass()));

        //类上的注释
        meta.setComments(this.generateComments(source.getAnnotations()).toArray(new String[]{}));
        //类上的注解
        meta.setAnnotations(this.converterAnnotations(source.getAnnotations(), javaClassMeta));

        if (count == 1) {
            if (javaClassMeta.isApiServiceClass()) {
                //spring的控制器  生成方法列表
                meta.setMethodMetas(this.converterMethodMetas(javaClassMeta.getMethodMetas(), javaClassMeta));
            } else {
                // 普通的java bean DTO  生成属性列表
                meta.setFiledMetas(this.converterFieldMetas(javaClassMeta.getFieldMetas(), javaClassMeta));
            }
        }
        if (count == 1) {
            //依赖列表
            meta.setDependencies(this.fetchDependencies(javaClassMeta.getDependencyList()));
        }

        Map<String, TypescriptClassMeta> metaDependencies = meta.getDependencies() == null ? new LinkedHashMap<>() : (Map<String, TypescriptClassMeta>) meta.getDependencies();

        Map<String/*类型，父类，接口，本身*/, CommonCodeGenClassMeta[]> superTypeVariables = new LinkedHashMap<>();

        //处理类上面的类型变量
        javaClassMeta.getSuperTypeVariables().forEach((superClazz, val) -> {

            TypescriptClassMeta typescriptClassMeta = this.parse(superClazz);

//            CommonCodeGenClassMeta[] classMetas = Arrays.stream(val).map(clazz -> {
//                TypescriptClassMeta classMeta = this.parse(clazz);
//                if (JavaTypeUtil.isComplex(clazz)) {
//                    metaDependencies.put(classMeta.getName(), classMeta);
//                }
//                return classMeta;
//            }).filter(Objects::nonNull).toArray(CommonCodeGenClassMeta[]::new);

            typescriptClassMeta.setTypeVariables(val);

            superTypeVariables.put(superClazz.getSimpleName(), new CommonCodeGenClassMeta[]{typescriptClassMeta});
        });
        meta.setDependencies(metaDependencies);
        meta.setSuperTypeVariables(superTypeVariables);

        HANDLE_RESULT_CACHE.put(source, meta);
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

        boolean isEnum = classMeta.getClazz().isEnum();

        List<TypescriptFieldMate> typescriptFieldMates = null;
        if (!isEnum) {

            //如果是java bean 需要合并get方法
            // 找出java bean中不存在属性定义的get或 is方法
            typescriptFieldMates = Arrays.stream(classMeta.getMethodMetas())
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
        } else {
            typescriptFieldMates = new ArrayList<>();
        }

        typescriptFieldMates.addAll(Arrays.stream(javaFieldMetas)
                .filter(javaFieldMeta -> !javaFieldMeta.getIsStatic() || isEnum)
                .map(javaFieldMeta -> {
                    TypescriptFieldMate typescriptFieldMate = new TypescriptFieldMate();

                    typescriptFieldMate.setName(javaFieldMeta.getName());
                    typescriptFieldMate.setAccessPermission(javaFieldMeta.getAccessPermission());

                    //注释来源于注解和java的类类型
                    List<String> comments = super.generateComments(javaFieldMeta.getAnnotations());
                    if (!isEnum) {
                        comments.addAll(super.generateComments(javaFieldMeta.getTypes(), false));
                    } else {
                        if (comments.size() == 0) {
                            log.error("枚举没有加上描述相关的注解");
                        }
                    }

                    //注解
                    typescriptFieldMate.setComments(comments.toArray(new String[]{}));

                    //注解
                    typescriptFieldMate.setAnnotations(this.converterAnnotations(javaFieldMeta.getAnnotations(), javaFieldMeta));

                    //是否必填
                    typescriptFieldMate.setRequired(javaFieldMeta.existAnnotation(NotNull.class));

                    //field 类型类别
                    Collection<TypescriptClassMeta> typescriptClassMetas = this.typescriptTypeMapping.mapping(javaFieldMeta.getTypes());

                    //从泛型中解析
                    Type[] typeVariables = javaFieldMeta.getTypeVariables();
                    if (typeVariables != null && typeVariables.length > 0) {

                        typescriptClassMetas.addAll(Arrays.stream(typeVariables)
                                .filter(Objects::nonNull)
                                .map(Type::getTypeName).map(name -> {
                                    TypescriptClassMeta typescriptClassMeta = new TypescriptClassMeta();
                                    BeanUtils.copyProperties(TypescriptClassMeta.TYPE_VARIABLE, typescriptClassMeta);
                                    typescriptClassMeta.setName(name);
                                    return typescriptClassMeta;
                                }).collect(Collectors.toList()));
                    }

                    if (typescriptClassMetas.size() > 0) {
                        //域对象类型描述
                        typescriptFieldMate.setFiledTypes(typescriptClassMetas.toArray(new TypescriptClassMeta[]{}));
                    } else {

                        //解析失败
                        throw new RuntimeException(String.format("解析类 %s 上的属性 %s 的类型 %s 失败",
                                classMeta.getClassName(),
                                javaFieldMeta.getName(),
                                this.classToNamedString(javaFieldMeta.getTypes())));

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
                .filter(javaMethodMeta -> this.genMatchingStrategy.isMatchMethod(javaMethodMeta))
                .map(javaMethodMeta -> {
                    CommonCodeGenMethodMeta genMethodMeta = new CommonCodeGenMethodMeta();
                    //method转换
                    genMethodMeta.setAccessPermission(javaMethodMeta.getAccessPermission());
                    //注解转注释
                    List<String> comments = super.generateComments(javaMethodMeta.getAnnotations());
                    comments.addAll(super.generateComments(javaMethodMeta.getReturnType(), true));
                    genMethodMeta.setComments(comments.toArray(new String[]{}));
                    genMethodMeta.setName(javaMethodMeta.getName());

                    //处理方法上的相关注解
                    genMethodMeta.setAnnotations(this.converterAnnotations(javaMethodMeta.getAnnotations(), javaMethodMeta));


                    //TODO support spring webflux

                    //处理返回值
                    List<TypescriptClassMeta> typescriptClassMetas = this.typescriptTypeMapping.mapping(javaMethodMeta.getReturnType());
                    if (!typescriptClassMetas.contains(TypescriptClassMeta.PROMISE)) {
                        typescriptClassMetas.add(0, TypescriptClassMeta.PROMISE);
                    }

                    if (typescriptClassMetas.size() > 0) {
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
                            TypescriptClassMeta typescriptClassMeta = this.parse(clazz);
                            BeanUtils.copyProperties(typescriptClassMeta, argsClassMeta);

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
                            List<String> generateComments = this.generateComments(annotations);
                            generateComments.addAll(this.generateComments(new Class[]{clazz}, false));
                            typescriptFieldMate.setComments(generateComments.toArray(new String[]{}));

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
//                    if (effectiveParams.size() == 0) {
//                        //无参数
//                        argsClassMeta.setName("{}");
//                    }
                    if (!StringUtils.hasText(argsClassMeta.getName())) {
                        //没有复杂对象的参数
                        String name = ToggleCaseUtil.toggleFirstChart(genMethodMeta.getName()) + "Req";
                        argsClassMeta.setName(name);
                        argsClassMeta.setPackagePath("req/" + name);
                    }
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
