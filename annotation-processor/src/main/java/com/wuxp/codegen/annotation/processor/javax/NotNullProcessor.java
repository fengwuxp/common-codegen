package com.wuxp.codegen.annotation.processor.javax;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.text.MessageFormat;

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
        public String toComment(Field annotationOwner) {

            return MessageFormat.format("属性：{0}为必填项，不能为空", annotationOwner.getName());
        }
    }
}
