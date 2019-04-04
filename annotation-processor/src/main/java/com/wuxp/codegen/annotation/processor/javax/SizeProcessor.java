package com.wuxp.codegen.annotation.processor.javax;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;

import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.text.MessageFormat;

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


    public abstract static class SizeMate implements AnnotationMate<Size>, Size {

        @Override
        public String toComment(Field annotationOwner) {

            return MessageFormat.format("属性：{0}输入字符串的最小长度为：{1}，输入字符串的最大长度为：{2}", annotationOwner.getName(), this.min(), this.max());
        }
    }
}
