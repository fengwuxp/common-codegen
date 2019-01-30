package com.wuxp.codegen.dragon.annotation.processor.javax;

import com.wuxp.codegen.dragon.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.dragon.annotation.processor.AnnotationMate;

import javax.validation.constraints.Size;
import java.lang.reflect.Field;

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

            return "属性：" + annotationOwner.getName() + "输入字符串的最小长度为：" +
                    this.min() +
                    "，输入字符串的最大长度为：" +
                    this.max();
        }
    }
}
