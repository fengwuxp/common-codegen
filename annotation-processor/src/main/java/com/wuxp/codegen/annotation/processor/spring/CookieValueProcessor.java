package com.wuxp.codegen.annotation.processor.spring;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author wxup
 * @see CookieValue
 * 处理 CookieValue 注解
 */
public class CookieValueProcessor extends AbstractAnnotationProcessor<CookieValue, CookieValueProcessor.CookieValueMate> {


    @Override
    public CookieValueProcessor.CookieValueMate process(CookieValue annotation) {

        return super.newProxyMate(annotation, CookieValueProcessor.CookieValueMate.class);
    }


    public abstract static class CookieValueMate implements AnnotationMate<CookieValue>, CookieValue {

        public CookieValueMate() {
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Field annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(CookieValue.class.getName());
            Map<String, String> arguments = new LinkedHashMap<>();
            String value = this.value();
            if (!StringUtils.hasText(value)) {
                value = this.name();
            }
            arguments.put("name", value);
            //注解位置参数
            List<String> positionArguments = new LinkedList<>(arguments.values());
            annotation.setNamedArguments(arguments)
                    .setPositionArguments(positionArguments);
            return annotation;
        }

        @Override
        public String toComment(Field annotationOwner) {

            return MessageFormat.format("属性：{0}是一个 cookie", annotationOwner.getName());
        }
    }
}
