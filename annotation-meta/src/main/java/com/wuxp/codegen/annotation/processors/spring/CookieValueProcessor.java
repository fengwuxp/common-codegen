package com.wuxp.codegen.annotation.processors.spring;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.NamedAnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.web.bind.annotation.CookieValue;

import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.wuxp.codegen.annotation.processors.spring.RequestParamProcessor.getRequestAnnotationDesc;

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


    public abstract static class CookieValueMate implements NamedAnnotationMate, CookieValue {

        protected final CookieValue cookieValue;

        public CookieValueMate(CookieValue cookieValue) {
            this.cookieValue = cookieValue;
        }

        @Override
        public String name() {
            return cookieValue.name();
        }

        @Override
        public String value() {
            return cookieValue.value();
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(CookieValue.class.getSimpleName());
            Map<String, String> arguments = new LinkedHashMap<>();
            String value = this.getParameterName(annotationOwner);
            arguments.put("name", MessageFormat.format("\"{0}\"", value));
            arguments.put("required", this.required() + "");
            //注解位置参数
            List<String> positionArguments = new LinkedList<>(arguments.values());
            annotation.setNamedArguments(arguments)
                    .setPositionArguments(positionArguments);
            return annotation;
        }

        @Override
        public String toComment(Parameter annotationOwner) {

            return getRequestAnnotationDesc(String.format("参数：%s是一个cookie value，%s", this.getParameterName(annotationOwner), required() ? "必填" : "非必填"), defaultValue());
        }
    }
}
