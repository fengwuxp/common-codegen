package com.wuxp.codegen.meta.annotations.factories.javax;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;
import java.text.MessageFormat;

/**
 * javax 验证注解处理
 *
 * @author wuxp
 * @see javax.validation.constraints.NotBlank
 */
public class NotBlanklMetaFactory extends AbstractAnnotationMetaFactory<NotBlank, NotBlanklMetaFactory.NotNullMate> {

    @Override
    public NotNullMate factory(NotBlank annotation) {

        return super.newProxyMate(annotation, NotNullMate.class);
    }

    public abstract static class NotNullMate extends AbstractAnnotationMate implements AnnotationMate, NotBlank {
        @Override
        public String toComment(Field annotationOwner) {
            return MessageFormat.format("{0} 约束条件：为必填项，不能为空", annotationOwner.getName());
        }
    }
}
