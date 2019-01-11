package com.wuxp.codegen.annotation.processor.javax;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;

import javax.validation.constraints.NotNull;

/**
 * javax 验证注解处理
 *
 * @see javax.validation.constraints.NotNull
 */
public class NotNullProcessor extends AbstractAnnotationProcessor<NotNull, NotNullProcessor.NotNullMate> {


    @Override
    public NotNullMate process(NotNull annotation) {

        return super.newProxyMate(annotation, NotNullMate.class);
    }


    public abstract static class NotNullMate implements AnnotationMate<NotNull>, NotNull {

        public NotNullMate() {
        }

        @Override
        public String toComment() {

            return "必填项，不能为空";
        }
    }
}
