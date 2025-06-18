package com.wuxp.codegen.meta.annotations.factories.javax;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;

import jakarta.validation.constraints.Size;
import java.lang.reflect.Field;
import java.text.MessageFormat;

/**
 * javax 验证注解处理
 *
 * @author wuxp
 * @see Size
 */
public class SizeMetaFactory extends AbstractAnnotationMetaFactory<Size, SizeMetaFactory.SizeMate> {


    @Override
    public SizeMate factory(Size annotation) {
        return super.newProxyMate(annotation, SizeMate.class);
    }


    public abstract static class SizeMate extends AbstractAnnotationMate implements AnnotationMate, Size {

        @Override
        public String toComment(Field annotationOwner) {
            return MessageFormat.format("{0} 约束条件：输入字符串的最小长度为：{1}，输入字符串的最大长度为：{2}", annotationOwner.getName(), this.min(), this.max());
        }
    }
}
