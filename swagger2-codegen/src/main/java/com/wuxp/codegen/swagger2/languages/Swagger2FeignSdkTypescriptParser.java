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
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * 基于swagger2 生成 feign sdk的 typeScript的 parser
 *
 * @author wuxp
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
        super.enhancedProcessingField(fieldMeta, javaFieldMeta, classMeta);
        if (javaFieldMeta == null || !Boolean.TRUE.equals(fieldMeta.getRequired())) {
            return;
        }
        Optional<ApiModelProperty> apiModelProperty = javaFieldMeta.getAnnotation(ApiModelProperty.class);
        if (apiModelProperty.isPresent()) {
            fieldMeta.setRequired(apiModelProperty.get().required());
            return;
        }
        Optional<ApiParam> apiParam = javaFieldMeta.getAnnotation(ApiParam.class);
        if (apiParam.isPresent()) {
            fieldMeta.setRequired(apiParam.get().required());
            return;
        }
        log.warn("类{}上的属性{}没有ApiModelProperty注解", classMeta.getClassName(), javaFieldMeta.getName());
    }


    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {
        super.enhancedProcessingMethod(methodMeta, javaMethodMeta, classMeta);
        if (!javaMethodMeta.existAnnotation(ApiImplicitParam.class, ApiImplicitParams.class)) {
            return;
        }
        CommonCodeGenClassMeta commonCodeGenClassMeta = methodMeta.getParams().get(DEFAULT_MARGE_PARAMS_NAME);
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
                                //是否必填
                                typescriptFieldMate.setRequired(typescriptFieldMate.getRequired() || apiImplicitParam.required());
                            });
                });
    }


}
