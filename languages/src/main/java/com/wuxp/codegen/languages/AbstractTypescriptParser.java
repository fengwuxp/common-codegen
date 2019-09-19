package com.wuxp.codegen.languages;

import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.factory.TypescriptLanguageMetaInstanceFactory;
import com.wuxp.codegen.mapping.TypescriptTypeMapping;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.constant.MappingAnnotationPropNameConstant;
import com.wuxp.codegen.model.constant.TypescriptFeignMediaTypeConstant;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 抽象的typescript parser
 */
@Slf4j
public abstract class AbstractTypescriptParser extends AbstractLanguageParser<TypescriptClassMeta, CommonCodeGenMethodMeta, TypescriptFieldMate> {


    public AbstractTypescriptParser(PackageMapStrategy packageMapStrategy,
                                    CodeGenMatchingStrategy genMatchingStrategy,
                                    Collection<CodeDetect> codeDetects) {
        super(new TypescriptLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
        this.typeMapping = new TypescriptTypeMapping(this);
    }

    {

        //根据java 类进行匹配
        codeGenMatchers.add(clazz -> clazz.isEnum() || JavaTypeUtil.isNoneJdkComplex(clazz) || clazz.isAnnotation());
    }

    @Override
    protected TypescriptFieldMate converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {

        TypescriptFieldMate typescriptFieldMate = super.converterField(javaFieldMeta, classMeta);

        if (typescriptFieldMate == null) {
            return null;
        }

        //是否必填
        typescriptFieldMate.setRequired(javaFieldMeta.existAnnotation(NotNull.class, NotBlank.class, NotEmpty.class));

//        this.enhancedProcessingField(typescriptFieldMate, javaFieldMeta, classMeta);

        return typescriptFieldMate;
    }


    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, TypescriptClassMeta codeGenClassMeta) {
        CommonCodeGenMethodMeta commonCodeGenMethodMeta = super.converterMethod(javaMethodMeta, classMeta, codeGenClassMeta);
        if (commonCodeGenMethodMeta == null) {
            return null;
        }

        //处理返回值
        Class<?>[] returnTypes = javaMethodMeta.getReturnType();
        Class<?> mapClazz = Arrays.stream(returnTypes)
                .filter(JavaTypeUtil::isMap)
                .findAny()
                .orElse(null);

        List<Class<?>> newTypes = Arrays.stream(returnTypes).collect(Collectors.toList());
//        Class<?>[] newTypes = (Class<?>[]) collect;

        //处理map 类型的对象
        if (mapClazz != null) {
            int length = newTypes.size();
            for (int i = 0; i < length; i++) {
                if (newTypes.get(i).equals(mapClazz)) {
                    int i1 = i + 1;
                    if (i1 >= length) {
                        //没有设置 Map 的key value的泛型
                        log.warn(MessageFormat.format("处理类：{0}上的方法：{1},发现非预期的情况", classMeta.getClassName(), javaMethodMeta.getName()));
                        newTypes.add(Object.class);
                        newTypes.add(Object.class);
                    }
                    Class<?> keyClazz = newTypes.get(i1);
                    if (!JavaTypeUtil.isJavaBaseType(keyClazz)) {
                        newTypes.set(i1, Object.class);
                    }
                    break;
                }
            }
        }
        Class[] newReturnTypes = newTypes.toArray(new Class[0]);
        List<TypescriptClassMeta> mapping = this.typeMapping.mapping(newReturnTypes);
        if (newTypes.size() > returnTypes.length) {
            //返回值类型列表发生变化，重新计算返回值类型
            returnTypes = newReturnTypes;
            javaMethodMeta.setReturnType(newReturnTypes);
            mapping = this.typeMapping.mapping(newReturnTypes);
            commonCodeGenMethodMeta.setReturnTypes(mapping.toArray(new CommonCodeGenClassMeta[0]));
        }


        if (!mapping.contains(TypescriptClassMeta.PROMISE)) {
            mapping.add(0, TypescriptClassMeta.PROMISE);
        }

        if (mapping.size() > 0) {
            //域对象类型描述
            commonCodeGenMethodMeta.setReturnTypes(mapping.toArray(new CommonCodeGenClassMeta[]{}));
        } else {
            //解析失败
            throw new RuntimeException(String.format("解析类 %s 上的方法 %s 的返回值类型 %s 失败",
                    classMeta.getClassName(),
                    javaMethodMeta.getName(),
                    this.classToNamedString(returnTypes)));
        }

        //将需要导入的加入依赖列表
//        Arrays.stream(commonCodeGenMethodMeta.getReturnTypes())
//                .filter(CommonCodeGenClassMeta::getNeedImport)
//                .forEach(returnType -> {
//                    ((Map<String, CommonCodeGenClassMeta>) codeGenClassMeta.getDependencies()).put(returnType.getName(), returnType);
//                });

        //增强处理
        this.enhancedProcessingMethod(commonCodeGenMethodMeta, javaMethodMeta, classMeta);

        return commonCodeGenMethodMeta;
    }

    /**
     * 增强处理 annotation
     *
     * @param codeGenAnnotation
     * @param annotation
     * @param annotationOwner
     */
    protected void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation, Object annotationOwner) {
        if (annotationOwner instanceof Class) {

        }
        if (annotationOwner instanceof Method) {
            //spring的mapping注解
            if (annotation.annotationType().getSimpleName().endsWith("Mapping")) {

                Method method = (Method) annotationOwner;
                //判断方法参数是否有RequestBody注解
                List<Annotation> annotationList = Arrays.stream(method.getParameterAnnotations())
                        .filter(Objects::nonNull)
                        .filter(annotations -> annotations.length > 0)
                        .map(Arrays::asList)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());


                String produces = codeGenAnnotation.getNamedArguments().get(MappingAnnotationPropNameConstant.PRODUCES);

                if (StringUtils.hasText(produces)) {
                    return;
                } else {
                    codeGenAnnotation.getNamedArguments().remove(MappingAnnotationPropNameConstant.PRODUCES);
                }

                //是否启用默认的 produces
//                boolean enableDefaultProduces = true;
//                if (!enableDefaultProduces) {
//                    return;
//                }
                boolean hasRequestBodyAnnotation = annotationList.size() > 0 && annotationList.stream()
                        .anyMatch(paramAnnotation -> RequestBody.class.equals(paramAnnotation.annotationType()));
                if (hasRequestBodyAnnotation) {
                    produces = TypescriptFeignMediaTypeConstant.JSON_UTF8;
                } else {
                    //如果没有 RequestBody 则认为是已表单的方式提交的参数
                    //是spring的Mapping注解
                    produces = TypescriptFeignMediaTypeConstant.FORM_DATA;
                }

                if (!StringUtils.hasText(produces)) {
                    return;
                }

                codeGenAnnotation.getNamedArguments().put(MappingAnnotationPropNameConstant.PRODUCES, produces);
            }
        }
    }


}
