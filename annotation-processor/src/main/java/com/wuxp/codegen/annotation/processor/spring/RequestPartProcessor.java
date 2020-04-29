package com.wuxp.codegen.annotation.processor.spring;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestPart;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author wxup
 * @see RequestPart
 * 处理 RequestPart 注解
 */
public class RequestPartProcessor extends AbstractAnnotationProcessor<RequestPart, RequestPartProcessor.RequestPartMate> {


    @Override
    public RequestPartProcessor.RequestPartMate process(RequestPart annotation) {

        return super.newProxyMate(annotation, RequestPartProcessor.RequestPartMate.class);
    }


    public abstract static class RequestPartMate implements AnnotationMate<RequestPart>, RequestPart {

        public RequestPartMate() {
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(RequestPart.class.getSimpleName());
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
        public String toComment(Parameter annotationOwner) {

            return MessageFormat.format("属性：{0}是一个 requset part", annotationOwner.getName());
        }
    }
}
