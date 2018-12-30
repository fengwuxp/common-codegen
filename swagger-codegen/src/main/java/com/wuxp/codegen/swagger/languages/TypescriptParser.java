package com.wuxp.codegen.swagger.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractTypescriptParser;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.swagger.annotations.ApiModelPropertyProcessor;
import com.wuxp.codegen.swagger.annotations.ApiProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;


import java.util.Collection;

/**
 * typeScript的 parser
 */
@Slf4j
public class TypescriptParser extends AbstractTypescriptParser {


    static {
        //添加swagger相关的注解处理器
        ANNOTATION_PROCESSOR_MAP.put(Api.class, new ApiProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiModelProperty.class, new ApiModelPropertyProcessor());
    }

    public TypescriptParser() {
        super(null, null);
    }

    public TypescriptParser(PackageMapStrategy packageMapStrategy, Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, codeDetects);
    }


    @Override
    protected void enhancedProcessingField(TypescriptFieldMate fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        ApiModelProperty apiModelProperty = javaFieldMeta.getAnnotation(ApiModelProperty.class);
        if (apiModelProperty == null) {
            log.warn("类{}上的属性{}没有ApiModelProperty注解", classMeta.getClassName(), javaFieldMeta.getName());
        } else {
            if (fieldMeta.getRequired() == null) {
                fieldMeta.setRequired(apiModelProperty.required());
            }
        }
    }


    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

    }
}
