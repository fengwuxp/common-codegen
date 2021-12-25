package com.wuxp.codegen.meta.annotations.factories.javax;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import javax.validation.constraints.Pattern;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * javax 验证注解处理
 *
 * @author wuxp
 * @see Pattern
 */
public class PatternMetaFactory extends AbstractAnnotationMetaFactory<Pattern, PatternMetaFactory.PatternMate> {


    @Override
    public PatternMate factory(Pattern annotation) {

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

            return MessageFormat.format("{0} 约束条件：匹配正则表达式{1}", annotationOwner.getName(), this.regexp());
        }
    }
}
