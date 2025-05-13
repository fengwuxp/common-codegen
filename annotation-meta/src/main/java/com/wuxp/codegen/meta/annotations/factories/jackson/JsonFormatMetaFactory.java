package com.wuxp.codegen.meta.annotations.factories.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;

import java.lang.reflect.Field;
import java.text.MessageFormat;

/**
 * @author wuxp
 * @date 2024-04-18 19:41
 * @see com.fasterxml.jackson.annotation.JsonFormat
 **/
public class JsonFormatMetaFactory extends AbstractAnnotationMetaFactory<JsonFormat, JsonFormatMetaFactory.JsonFormatMate> {

    @Override
    public JsonFormatMate factory(JsonFormat annotation) {
        return super.newProxyMate(annotation, JsonFormatMetaFactory.JsonFormatMate.class);
    }

    public abstract static class JsonFormatMate extends AbstractAnnotationMate implements AnnotationMate, JsonFormat {
        @Override
        public String toComment(Field annotationOwner) {
            return MessageFormat.format("format pattern：{0} ", annotationOwner.getName());
        }
    }
}
