package com.wuxp.codegen.swagger.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.annotations.ApiModelProperty;

/**
 * swagger 注解处理
 *
 * @see ApiModelProperty
 */
public class ApiModelPropertyProcessor extends AbstractAnnotationProcessor<ApiModelProperty, ApiModelPropertyProcessor.ApiModelPropertyMate> {


    @Override
    public ApiModelPropertyMate process(ApiModelProperty annotation) {
        return this.newProxyMate(annotation, ApiModelPropertyMate.class);
    }


    public static abstract class ApiModelPropertyMate implements AnnotationMate<ApiModelProperty>, ApiModelProperty {


        @Override
        public String toComment() {
            return null;
        }
    }
}
