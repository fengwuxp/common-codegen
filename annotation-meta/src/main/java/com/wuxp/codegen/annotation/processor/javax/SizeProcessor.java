package com.wuxp.codegen.annotation.processor.javax;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * javax 验证注解处理
 *
 * @see Size
 */
public class SizeProcessor extends AbstractAnnotationProcessor<Size, SizeProcessor.SizeMate> {


    @Override
    public SizeMate process(Size annotation) {
        return super.newProxyMate(annotation, SizeMate.class);
    }


    public abstract static class SizeMate implements AnnotationMate, Size {

        @Override
        public CommonCodeGenAnnotation toAnnotation(Field annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(Size.class.getName());
            Map<String, String> namedArguments = new HashMap<>();
            namedArguments.put("message", this.message());
            namedArguments.put("max", MessageFormat.format("{0}", this.max()));
            namedArguments.put("min", MessageFormat.format("{0}", this.min()));
            annotation.setNamedArguments(namedArguments);
            return annotation;
        }

        @Override
        public String toComment(Field annotationOwner) {

            return MessageFormat.format("属性：{0}输入字符串的最小长度为：{1}，输入字符串的最大长度为：{2}", annotationOwner.getName(), this.min(), this.max());
        }
    }
}
