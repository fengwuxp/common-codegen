package com.wuxp.codegen.annotation.processor.javax;

import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationToString;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * javax 验证注解处理
 *
 * @see javax.validation.constraints.NotNull
 */
public class NotNullProcessor implements AnnotationProcessor<NotNullProcessor.NotNullMate, NotNull> {


    @Override
    public NotNullMate process(NotNull annotation) {
        if (annotation == null) {
            return null;
        }
        return NotNullMate.builder()
                .message(annotation.message())
                .build();
    }


    @Data
    @Builder
    public static class NotNullMate implements AnnotationToString {


        private String message;


        @Override
        public String toComment() {
            return "必填项，不能为空";
        }
    }
}
