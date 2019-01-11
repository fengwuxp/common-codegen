package com.wuxp.codegen.swagger.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.annotations.ApiModel;
import org.springframework.util.StringUtils;

/**
 * swagger 注解处理
 *
 * @see ApiModel
 */
public class ApiModelProcessor extends AbstractAnnotationProcessor<ApiModel, ApiModelProcessor.ApiModelMate> {


    @Override
    public ApiModelMate process(ApiModel annotation) {
        return this.newProxyMate(annotation, ApiModelMate.class);
    }

    public abstract static class ApiModelMate implements AnnotationMate<ApiModel>, ApiModel {


        @Override
        public String toComment() {
            String description = this.description();
            if (!StringUtils.hasText(description)) {
                return this.value();
            }
            return description;
        }
    }
}
