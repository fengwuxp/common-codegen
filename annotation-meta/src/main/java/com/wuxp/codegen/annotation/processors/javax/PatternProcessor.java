package com.wuxp.codegen.annotation.processors.javax;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

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


    public abstract static class PatternMate implements AnnotationMate, Pattern {


        @Override
        public CommonCodeGenAnnotation toAnnotation(Field annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(Pattern.class.getName());
            Map<String, String> namedArguments = new HashMap<>();
            namedArguments.put("message",this.message());
            namedArguments.put("regexp",this.regexp());
            annotation.setNamedArguments(namedArguments);
            return annotation;
        }

        @Override
        public String toComment(Field annotationOwner) {

            return MessageFormat.format("属性：{0}匹配的规则为：{1}", annotationOwner.getName(), this.regexp());
        }
    }
}
