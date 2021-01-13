package com.wuxp.codegen.swagger2.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import io.swagger.annotations.ApiModel;
import org.springframework.util.StringUtils;

/**
 * swagger2 注解处理
 *
 * @author wuxp
 * @see ApiModel
 */
public class ApiModelProcessor extends AbstractAnnotationProcessor<ApiModel, ApiModelProcessor.ApiModelMate> {


    @Override
    public ApiModelMate process(ApiModel annotation) {
        return this.newProxyMate(annotation, ApiModelMate.class);
    }

    public abstract static class ApiModelMate implements AnnotationMate, ApiModel {


        @Override
        public String toComment(Class<?> annotationOwner) {
            String description = this.description();
            if (!StringUtils.hasText(description)) {
                return this.value();
            }
            return description;
        }
    }
}
