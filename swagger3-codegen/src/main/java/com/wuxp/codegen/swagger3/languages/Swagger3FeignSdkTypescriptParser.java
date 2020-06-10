package com.wuxp.codegen.swagger3.languages;

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
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 基于 open api3 生成 feign sdk的 typeScript的 parser
 * @author wxup
 */
@Slf4j
public class Swagger3FeignSdkTypescriptParser extends AbstractTypescriptParser {


    public Swagger3FeignSdkTypescriptParser(PackageMapStrategy packageMapStrategy,
                                            CodeGenMatchingStrategy genMatchingStrategy,
                                            Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects);
    }


    @Override
    protected void enhancedProcessingField(TypescriptFieldMate fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        if (javaFieldMeta == null) {
            return;
        }
        Schema schema = javaFieldMeta.getAnnotation(Schema.class);
        Parameter parameter = javaFieldMeta.getAnnotation(Parameter.class);
        if (schema == null && parameter == null) {
            log.warn("类{}上的属性{}没有Schema注解", classMeta.getClassName(), javaFieldMeta.getName());
            return;
        }

        if (fieldMeta.getRequired() == null) {
            if (schema != null) {
                fieldMeta.setRequired(schema.required());
            } else {
                fieldMeta.setRequired(parameter.required());
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
//        if (javaMethodMeta.existAnnotation(Hidden.class)) {
//            return null;
//        }

        return super.converterMethod(javaMethodMeta, classMeta, codeGenClassMeta);
    }

    @Override
    protected TypescriptFieldMate converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        if (javaFieldMeta.existAnnotation(Hidden.class)) {
            return null;
        }
        return super.converterField(javaFieldMeta, classMeta);
    }

    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

        if (!javaMethodMeta.existAnnotation(Parameters.class)) {
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
                                if (annotationType.equals(Parameters.class)) {
                                    Parameters apiImplicitParams = (Parameters) annotation;
                                    Optional<Parameter> optionalApiImplicitParam = Arrays.stream(apiImplicitParams.value())
                                            .filter(apiImplicitParam -> name.equals(apiImplicitParam.name()))
                                            .findFirst();
                                    if (optionalApiImplicitParam.isPresent()) {
                                        return optionalApiImplicitParam.get();
                                    }

                                } else if (annotationType.equals(Parameter.class)) {
                                    if (name.equals(((Parameter) annotation).name())) {
                                        return annotation;
                                    }
                                }
                                return null;
                            })
                            .filter(Objects::nonNull)
                            .forEach(annotation -> {

                                Parameter apiImplicitParam = (Parameter) annotation;

                                //强化注释
                                List<String> comments = new ArrayList<>(Arrays.asList(genFiledMeta.getComments()));
                                comments.add((apiImplicitParam).name());
                                typescriptFieldMate.setComments(comments.toArray(new String[]{}));

                                //是否必填
                                typescriptFieldMate.setRequired(typescriptFieldMate.getRequired() || apiImplicitParam.required());

                            });
                });
    }


}
