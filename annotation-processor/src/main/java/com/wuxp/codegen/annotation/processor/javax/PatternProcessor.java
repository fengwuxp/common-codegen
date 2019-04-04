package com.wuxp.codegen.annotation.processor.javax;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;

import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;
import java.text.MessageFormat;

/**
 * javax 验证注解处理
 *
 * @see Pattern
 */
public class PatternProcessor extends AbstractAnnotationProcessor<Pattern, PatternProcessor.PatternMate> {


    @Override
    public PatternMate process(Pattern annotation) {

        return super.newProxyMate(annotation, PatternMate.class);
    }


    public abstract static class PatternMate implements AnnotationMate<Pattern>, Pattern {

        @Override
        public String toComment(Field annotationOwner) {

            return MessageFormat.format("属性：{0}匹配的规则为：{1}", annotationOwner.getName(), this.regexp());
        }
    }
}
