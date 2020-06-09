package com.wuxp.codegen.swagger2.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractTypescriptParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 基于swagger2 生成 feign sdk的 typeScript的 parser
 */
@Slf4j
public class Swagger2FeignSdkTypescriptParser extends AbstractTypescriptParser {


    public Swagger2FeignSdkTypescriptParser(PackageMapStrategy packageMapStrategy,
                                            CodeGenMatchingStrategy genMatchingStrategy,
                                            Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects);
    }


    @Override
    protected void enhancedProcessingField(TypescriptFieldMate fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        if (javaFieldMeta == null) {
            return;
        }
        ApiModelProperty apiModelProperty = javaFieldMeta.getAnnotation(ApiModelProperty.class);
        ApiParam apiParam = javaFieldMeta.getAnnotation(ApiParam.class);
        if (apiModelProperty == null && apiParam == null) {
            log.warn("类{}上的属性{}没有ApiModelProperty注解", classMeta.getClassName(), javaFieldMeta.getName());
            return;
        }

        if (!Boolean.TRUE.equals(fieldMeta.getRequired())) {
            if (apiModelProperty != null) {
                fieldMeta.setRequired(apiModelProperty.required());
            } else {
                fieldMeta.setRequired(apiParam.required());
            }
        }
    }


    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, TypescriptClassMeta codeGenClassMeta) {

//        if (!AccessPermission.PUBLIC.equals(javaMethodMeta.getAccessPermission())) {
//            return null;
//        }
//
//        if (!javaMethodMeta.existAnnotation(
//                SpringAnnotationClassConstant.SPRING_MAPPING_ANNOTATIONS
//        )) {
//            return null;
//        }
//
//        if (javaMethodMeta.existAnnotation(ApiIgnore.class)) {
//            return null;
//        }

        return super.converterMethod(javaMethodMeta, classMeta, codeGenClassMeta);
    }

    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

        if (!javaMethodMeta.existAnnotation(ApiImplicitParam.class, ApiImplicitParams.class)) {
            return;
        }
        CommonCodeGenClassMeta commonCodeGenClassMeta = methodMeta.getParams().get("req");
        if (commonCodeGenClassMeta == null) {
            return;
        }
        Arrays.stream(commonCodeGenClassMeta.getFieldMetas())
                .forEach(genFiledMeta -> {
                    final TypescriptFieldMate typescriptFieldMate = (TypescriptFieldMate) genFiledMeta;
                    final String name = typescriptFieldMate.getName();
                    Arrays.stream(javaMethodMeta.getAnnotations())
                            .map(annotation -> {
                                Class<? extends Annotation> annotationType = annotation.annotationType();
                                if (annotationType.equals(ApiImplicitParams.class)) {
                                    ApiImplicitParams apiImplicitParams = (ApiImplicitParams) annotation;
                                    Optional<ApiImplicitParam> optionalApiImplicitParam = Arrays.stream(apiImplicitParams.value())
                                            .filter(apiImplicitParam -> name.equals(apiImplicitParam.name()))
                                            .findFirst();
                                    if (optionalApiImplicitParam.isPresent()) {
                                        return optionalApiImplicitParam.get();
                                    }

                                } else if (annotationType.equals(ApiImplicitParam.class)) {
                                    if (name.equals(((ApiImplicitParam) annotation).name())) {
                                        return annotation;
                                    }
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .forEach(annotation -> {

                                ApiImplicitParam apiImplicitParam = (ApiImplicitParam) annotation;

                                //强化注释
                                List<String> comments = new ArrayList<>(Arrays.asList(genFiledMeta.getComments()));
                                comments.add((apiImplicitParam).value());
                                typescriptFieldMate.setComments(comments.toArray(new String[]{}));

                                //是否必填
                                typescriptFieldMate.setRequired(typescriptFieldMate.getRequired() || apiImplicitParam.required());

                            });
                });
    }


}
