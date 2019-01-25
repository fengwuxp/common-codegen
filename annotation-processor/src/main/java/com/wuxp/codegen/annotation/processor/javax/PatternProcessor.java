package com.wuxp.codegen.annotation.processor.javax;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;

import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;

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

            return "属性：" + annotationOwner.getName() + "匹配的规则为：" + this.regexp();
        }
    }
}
