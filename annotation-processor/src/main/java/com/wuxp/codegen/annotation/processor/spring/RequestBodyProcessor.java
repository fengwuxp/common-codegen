package com.wuxp.codegen.annotation.processor.spring;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxup
 * @see RequestBody
 * 处理 RequestBody 注解
 */
public class RequestBodyProcessor extends AbstractAnnotationProcessor<RequestBody, RequestBodyProcessor.RequestBodyMate> {


    @Override
    public RequestBodyProcessor.RequestBodyMate process(RequestBody annotation) {

        return super.newProxyMate(annotation, RequestBodyProcessor.RequestBodyMate.class);
    }


    public abstract static class RequestBodyMate implements AnnotationMate<RequestBody>, RequestBody {

        public RequestBodyMate() {
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Field annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(RequestBody.class.getName());
            Map<String, String> namedArguments = new HashMap<>();
            namedArguments.put("required", this.required() + "");
            annotation.setNamedArguments(namedArguments);
            return annotation;
        }

        @Override
        public String toComment(Field annotationOwner) {

            return MessageFormat.format("属性：{0}是一个 cookie", annotationOwner.getName());
        }
    }
}
