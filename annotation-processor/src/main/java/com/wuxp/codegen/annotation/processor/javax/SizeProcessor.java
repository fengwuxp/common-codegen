package com.wuxp.codegen.annotation.processor.javax;

import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationToString;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * javax 验证注解处理
 *
 * @see Size
 */
public class SizeProcessor implements AnnotationProcessor<SizeProcessor.SizeMate, Size> {


    @Override
    public SizeMate process(Size annotation) {
        if (annotation == null) {
            return null;
        }
        return SizeMate.builder()
                .max(annotation.max())
                .min(annotation.min())
                .message(annotation.message())
                .build();
    }


    @Data
    @Builder
    public static class SizeMate implements AnnotationToString {

        /**
         * 输入字符串的最小长度
         */
        private int min;

        /**
         * 输入字符串的最大长度
         */
        private int max;

        private String message;


        @Override
        public String toComment() {

            return "输入字符串的最小长度为：" +
                    this.min +
                    "，输入字符串的最大长度为：" +
                    this.max;
        }
    }
}
