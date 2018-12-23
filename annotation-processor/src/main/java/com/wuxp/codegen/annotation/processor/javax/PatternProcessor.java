package com.wuxp.codegen.annotation.processor.javax;

import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationToString;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * javax 验证注解处理
 *
 * @see Pattern
 */
public class PatternProcessor implements AnnotationProcessor<PatternProcessor.PatternMate, Pattern> {


    @Override
    public PatternMate process(Pattern annotation) {
        if (annotation == null) {
            return null;
        }
        return PatternMate.builder()
                .flags(annotation.flags())
                .regexp(annotation.regexp())
                .message(annotation.message())
                .build();
    }


    @Data
    @Builder
    public static class PatternMate implements AnnotationToString {

        /**
         * 正则表达式
         */
        private String regexp;

        /**
         *匹配模式
         */
        private Pattern.Flag[] flags;

        private String message;


        @Override
        public String toComment() {

            return "";
        }
    }
}
