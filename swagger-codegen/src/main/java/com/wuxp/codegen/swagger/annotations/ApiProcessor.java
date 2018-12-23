package com.wuxp.codegen.swagger.annotations;

import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationToString;
import io.swagger.annotations.Api;
import lombok.Builder;
import lombok.Data;

/**
 * swagger 注解处理
 *
 * @see Api
 */
public class ApiProcessor implements AnnotationProcessor<ApiProcessor.ApiMate, Api> {


    @Override
    public ApiMate process(Api annotation) {
        if (annotation == null) {
            return null;
        }
        return ApiMate.builder()
                .tags(annotation.tags())
                .value(annotation.value())
                .build();
    }

    @Data
    @Builder
    public static class ApiMate implements AnnotationToString {


        String value;

        String[] tags;


        @Override
        public String toComment() {
            if (tags.length == 0) {
                return this.value;
            }

            return tags[0];
        }
    }
}
