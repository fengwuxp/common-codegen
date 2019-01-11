package com.wuxp.codegen.swagger.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.StringUtils;

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


    public abstract static class ApiModelPropertyMate implements AnnotationMate<ApiModelProperty>, ApiModelProperty {


        @Override
        public String toComment() {
            String notes = this.notes();
            if (!StringUtils.hasText(notes)) {
                return this.value();
            }
            return notes;
        }
    }
}
