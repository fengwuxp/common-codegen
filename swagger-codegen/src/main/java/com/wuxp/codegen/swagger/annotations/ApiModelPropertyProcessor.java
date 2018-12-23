package com.wuxp.codegen.swagger.annotations;

import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationToString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

public class ApiModelPropertyProcessor implements AnnotationProcessor<ApiModelPropertyProcessor.ApiModelPropertyMate, ApiModelProperty> {


    @Override
    public ApiModelPropertyMate process(ApiModelProperty annotation) {
        if (annotation == null) {
            return null;
        }
        return ApiModelPropertyMate.builder()
                .build();
    }

    @Data
    @Builder
    public static class ApiModelPropertyMate implements AnnotationToString {


        @Override
        public String toComment() {
            return null;
        }
    }
}
