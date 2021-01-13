package com.wuxp.codegen.annotation.processors.spring;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.NamedAnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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


    public abstract static class RequestBodyMate implements NamedAnnotationMate, RequestBody {

        public RequestBodyMate() {
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(RequestBody.class.getSimpleName());
            Map<String, String> namedArguments = new HashMap<>();
            namedArguments.put("required", this.required() + "");
            //注解位置参数
            List<String> positionArguments = new LinkedList<>(namedArguments.values());
            annotation.setNamedArguments(namedArguments)
                    .setPositionArguments(positionArguments);
            return annotation;
        }

        @Override
        public String toComment(Parameter annotationOwner) {
            return String.format("参数：%s是一个RequestBody, %s", this.getParameterName(annotationOwner), required() ? "必填" : "非必填");
        }
    }
}
