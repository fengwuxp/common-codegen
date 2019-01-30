package com.wuxp.codegen.dragon.swagger2.languages;

import com.wuxp.codegen.dragon.core.CodeDetect;
import com.wuxp.codegen.dragon.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.dragon.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.dragon.languages.AbstractTypescriptParser;
import com.wuxp.codegen.dragon.swagger2.annotations.*;
import com.wuxp.codegen.dragon.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.dragon.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.dragon.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.dragon.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.dragon.model.languages.typescript.TypescriptFieldMate;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * swagger2 typeScript的 parser
 */
@Slf4j
public class TypescriptParser extends AbstractTypescriptParser {


    static {
        //添加swagger相关的注解处理器
        ANNOTATION_PROCESSOR_MAP.put(Api.class, new ApiProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiModel.class, new ApiModelProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiModelProperty.class, new ApiModelPropertyProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiOperation.class, new ApiOperationProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiParam.class, new ApiParamProcessor());
    }


    public TypescriptParser(PackageMapStrategy packageMapStrategy, CodeGenMatchingStrategy genMatchingStrategy, Collection<CodeDetect> codeDetects) {
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

        if (fieldMeta.getRequired() == null) {
            if (apiModelProperty != null) {
                fieldMeta.setRequired(apiModelProperty.required());
            } else {
                fieldMeta.setRequired(apiParam.required());
            }

        }
    }


    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {


    }


}
