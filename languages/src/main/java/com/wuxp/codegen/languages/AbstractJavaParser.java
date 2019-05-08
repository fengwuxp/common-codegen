package com.wuxp.codegen.languages;


import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.utils.ToggleCaseUtil;
import com.wuxp.codegen.languages.factory.JavaLanguageMetaInstanceFactory;
import com.wuxp.codegen.mapping.JavaTypeMapping;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽象的java parser
 */
@Slf4j
public class AbstractJavaParser extends AbstractLanguageParser<JavaCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {

    /**
     * 映射java类和typeScript类之间的关系
     */
    protected TypeMapping<Class<?>, List<JavaCodeGenClassMeta>> javaTypeMapping = new JavaTypeMapping(this);

    public AbstractJavaParser(PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects) {
        this(null,
                new JavaLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
    }

    public AbstractJavaParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                              LanguageMetaInstanceFactory languageMetaInstanceFactory,
                              PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects) {
        super(javaParser, languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects);

    }


    @Override
    protected CommonCodeGenFiledMeta converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {

        if (javaFieldMeta == null) {
            return null;
        }


        boolean isEnum = classMeta.getClazz().isEnum();
        if (javaFieldMeta.getIsStatic() && !isEnum) {
            //不处理静态类型的字段
            return null;
        }
        CommonCodeGenFiledMeta commonCodeGenFiledMeta = new CommonCodeGenFiledMeta();

        commonCodeGenFiledMeta.setName(javaFieldMeta.getName());
        commonCodeGenFiledMeta.setAccessPermission(javaFieldMeta.getAccessPermission());

        //注释来源于注解和java的类类型
        List<String> comments = super.generateComments(javaFieldMeta.getAnnotations(), javaFieldMeta.getField());
        if (!isEnum) {
            comments.addAll(super.generateComments(javaFieldMeta.getTypes(), false));
        } else {
            if (comments.size() == 0) {
                comments.add(javaFieldMeta.getName());
                log.error("枚举没有加上描述相关的注解");
            }
        }

        //注解
        commonCodeGenFiledMeta.setComments(comments.toArray(new String[]{}));

        //注解
        commonCodeGenFiledMeta.setAnnotations(this.converterAnnotations(javaFieldMeta.getAnnotations(), javaFieldMeta.getField()));


        //field 类型类别
        Collection<JavaCodeGenClassMeta> commonCodeGenClassMetas = this.javaTypeMapping.mapping(javaFieldMeta.getTypes());

        //从泛型中解析
        Type[] typeVariables = javaFieldMeta.getTypeVariables();
        if (typeVariables != null && typeVariables.length > 0) {

            commonCodeGenClassMetas.addAll(Arrays.stream(typeVariables)
                    .filter(Objects::nonNull)
                    .map(Type::getTypeName).map(name -> {
                        JavaCodeGenClassMeta javaCodeGenClassMeta = new JavaCodeGenClassMeta();
                        BeanUtils.copyProperties(JavaCodeGenClassMeta.TYPE_VARIABLE, javaCodeGenClassMeta);
                        javaCodeGenClassMeta.setName(name);
                        return javaCodeGenClassMeta;
                    }).collect(Collectors.toList()));
        }

        if (commonCodeGenClassMetas.size() > 0) {
            //域对象类型描述
            commonCodeGenFiledMeta.setFiledTypes(commonCodeGenClassMetas.toArray(new CommonCodeGenClassMeta[]{}));
        } else {

            //解析失败
            throw new RuntimeException(String.format("解析类 %s 上的属性 %s 的类型 %s 失败",
                    classMeta.getClassName(),
                    javaFieldMeta.getName(),
                    this.classToNamedString(javaFieldMeta.getTypes())));
        }


        //TODO 注解转化

        //增强处理
        this.enhancedProcessingField(commonCodeGenFiledMeta, javaFieldMeta, classMeta);

        return commonCodeGenFiledMeta;
    }

    @Override
    protected void enhancedProcessingField(CommonCodeGenFiledMeta fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {

    }

    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, JavaCodeGenClassMeta codeGenClassMeta) {
        if (javaMethodMeta == null) {
            return null;
        }

        CommonCodeGenMethodMeta genMethodMeta = new CommonCodeGenMethodMeta();
        //method转换
        genMethodMeta.setAccessPermission(javaMethodMeta.getAccessPermission());
        //注解转注释
        List<String> comments = super.generateComments(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod());
        comments.addAll(super.generateComments(javaMethodMeta.getReturnType(), true));
        genMethodMeta.setComments(comments.toArray(new String[]{}));
        genMethodMeta.setName(javaMethodMeta.getName());

        //处理方法上的相关注解
        genMethodMeta.setAnnotations(this.converterAnnotations(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod()));


        //TODO support spring webflux

        //处理返回值
        List<JavaCodeGenClassMeta> mapping = this.javaTypeMapping.mapping(javaMethodMeta.getReturnType());

        if (!mapping.contains(JavaCodeGenClassMeta.RX_JAVA2_OBSERVABLE)) {
            mapping.add(0, JavaCodeGenClassMeta.RX_JAVA2_OBSERVABLE);
        }

        if (mapping.size() > 0) {
            //域对象类型描述
            genMethodMeta.setReturnTypes(mapping.toArray(new CommonCodeGenClassMeta[]{}));
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
                    .filter(this.packageNameCodeGenMatcher::match)
                    .toArray(Class<?>[]::new);
            if (array.length == 0) {
                return;
            }
            effectiveParams.put(key, array);
        });

        //2: 合并参数列表，将参数列表中的简单类型参数和复杂的类型参数合并到一个列表中
        //2.1：遍历展开参数列表

        final Set<CommonCodeGenFiledMeta> commonCodeGenFiledMetas = new LinkedHashSet<>();
        //参数的元数据类型信息
        final JavaCodeGenClassMeta argsClassMeta = new JavaCodeGenClassMeta();


        effectiveParams.forEach((key, classes) -> {

            Class<?> clazz = classes[0];
            if (JavaTypeUtil.isNoneJdkComplex(clazz)) {
                JavaCodeGenClassMeta commonCodeGenClassMeta = this.parse(clazz);
                if (commonCodeGenClassMeta != null) {
                    BeanUtils.copyProperties(commonCodeGenClassMeta, argsClassMeta);
                }
            } else if (clazz.isEnum()) {
                //枚举
                CommonCodeGenFiledMeta fieldMate = new CommonCodeGenFiledMeta();
                commonCodeGenFiledMetas.add(fieldMate);

            } else {

                Set<Class<?>> otherDependencies = new HashSet<>();

                if (clazz.isArray()) {
                    //数组
                    otherDependencies.add(clazz.getComponentType());
                } else if (JavaTypeUtil.isCollection(clazz)) {
                    //集合
                    otherDependencies.addAll(Arrays.asList(classes));
                } else if (JavaTypeUtil.isMap(clazz)) {
                    //map
                    otherDependencies.addAll(Arrays.asList(classes));
                } else if (JavaTypeUtil.isJavaBaseType(clazz)) {
                    //简单数据类型
                } else {
                    log.warn("未处理的类型{}", clazz.getName());
                }

                otherDependencies.stream().filter(JavaTypeUtil::isNoneJdkComplex).forEach(c -> {
                    JavaCodeGenClassMeta commonCodeGenClassMeta = this.parse(c);
                    ((Map<String, JavaCodeGenClassMeta>) argsClassMeta.getDependencies()).put(commonCodeGenClassMeta.getName(), commonCodeGenClassMeta);
                });

                //注释
                Annotation[] annotations = javaMethodMeta.getParamAnnotations().get(key);

                //TODO 参数是否必须 是否为控制器  是否存在javax的验证注解、或者spring mvc相关注解 required=true 或者是swagger注解
//                    Arrays.stream(annotations).macth(annotation -> {
//                        annotation.annotationType().equals(Reque)
//                        return true;
//                    })
                JavaFieldMeta javaFieldMeta = new JavaFieldMeta();
                javaFieldMeta.setTypes(classes)
                        .setIsTransient(false)
                        .setIsVolatile(false);
                javaFieldMeta.setAccessPermission(AccessPermission.PUBLIC);
                javaFieldMeta.setAnnotations(annotations);
                javaFieldMeta.setName(key);
                CommonCodeGenFiledMeta commonCodeGenFiledMeta = this.converterField(javaFieldMeta, classMeta);

                if (commonCodeGenFiledMeta != null) {
                    this.enhancedProcessingField(commonCodeGenFiledMeta, javaFieldMeta, classMeta);
                    commonCodeGenFiledMetas.add(commonCodeGenFiledMeta);
                }
            }

        });
        //3: 重组，使用第二步得到的列表构建一个信息的 TypescriptClassMeta对象，类型为typescript的interface
        argsClassMeta.setClassType(ClassType.INTERFACE);
        if (argsClassMeta.getFiledMetas() != null) {
            Arrays.asList(argsClassMeta.getFiledMetas()).forEach(genFiledMeta -> {
                CommonCodeGenFiledMeta commonCodeGenFiledMeta = new CommonCodeGenFiledMeta();
                BeanUtils.copyProperties(genFiledMeta, commonCodeGenFiledMeta);
                boolean isExist = commonCodeGenFiledMetas.stream()
                        .filter(c -> c.getName().equals(genFiledMeta.getName()))
                        .toArray().length > 0;
                if (isExist) {
                    log.error("{}方法中的参数{}在类{}中已经存在", javaMethodMeta.getName(), genFiledMeta.getName(), argsClassMeta.getPackagePath() + argsClassMeta.getName());
                } else {
                    commonCodeGenFiledMetas.add(commonCodeGenFiledMeta);
                }

            });
        }
        argsClassMeta.setFiledMetas(commonCodeGenFiledMetas.toArray(new CommonCodeGenFiledMeta[]{}));
        if (!StringUtils.hasText(argsClassMeta.getName())) {
            //没有复杂对象的参数
            String name = MessageFormat.format("{0}Req", ToggleCaseUtil.toggleFirstChart(genMethodMeta.getName()));
            argsClassMeta.setName(name);
            argsClassMeta.setPackagePath(this.packageMapStrategy.genPackagePath(new String[]{"req", name}));
            //这个时候没有依赖
            argsClassMeta.setAnnotations(new CommonCodeGenAnnotation[]{});
            argsClassMeta.setComments(new String[]{"合并方法参数生成的类"});
        } else {
            boolean hasComplex = false, hasSimple = false;
            for (Class[] classes : effectiveParams.values()) {
                Class<?> clazz = classes[0];
                if (JavaTypeUtil.isNoneJdkComplex(clazz)) {
                    hasComplex = true;
                } else {
                    hasSimple = true;
                }
            }
            if (hasComplex && hasSimple) {
                //参数列表中有复杂对象，并且有额外的简单对象，将类的名称替换，使用方法的名称,重新生成过一个新的对象
                String name = MessageFormat.format("{0}Req", ToggleCaseUtil.toggleFirstChart(genMethodMeta.getName()));
                argsClassMeta.setPackagePath(argsClassMeta.getPackagePath().replace(argsClassMeta.getName(), name));
                argsClassMeta.setName(name);
            }

        }
        //加入依赖列表
        Map<String, ? extends CommonCodeGenClassMeta> dependencies = codeGenClassMeta.getDependencies();


        ((Map<String, JavaCodeGenClassMeta>) dependencies).put(argsClassMeta.getName(), argsClassMeta);

        LinkedHashMap<String, CommonCodeGenClassMeta> params = new LinkedHashMap<>();
        codeGenClassMeta.setDependencies(dependencies);
        //请求参数名称，固定为req
        params.put("req", argsClassMeta);
        genMethodMeta.setParams(params);

        //增强处理
        this.enhancedProcessingMethod(genMethodMeta, javaMethodMeta, classMeta);

        return genMethodMeta;
    }

    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

    }


}
