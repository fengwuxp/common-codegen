package com.wuxp.codegen.annotation.processors.javax;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

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


    public abstract static class NotNullMate implements AnnotationMate, NotNull {

        public NotNullMate() {
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Field annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(NotNull.class.getName());
            Map<String, String> namedArguments = new HashMap<>();
            namedArguments.put("message",this.message());
            annotation.setNamedArguments(namedArguments);
            return annotation;
        }

        @Override
        public String toComment(Field annotationOwner) {

            return MessageFormat.format("属性：{0}为必填项，不能为空", annotationOwner.getName());
        }
    }
}
