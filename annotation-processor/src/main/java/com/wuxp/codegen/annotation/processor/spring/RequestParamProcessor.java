package com.wuxp.codegen.annotation.processor.spring;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author wxup
 * @see RequestParam
 * 处理 RequestParam 注解
 */
public class RequestParamProcessor extends AbstractAnnotationProcessor<RequestParam, RequestParamProcessor.RequestParamMate> {


    @Override
    public RequestParamProcessor.RequestParamMate process(RequestParam annotation) {

        return super.newProxyMate(annotation, RequestParamProcessor.RequestParamMate.class);
    }


    public abstract static class RequestParamMate implements AnnotationMate<RequestParam>, RequestParam {

        public RequestParamMate() {
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(RequestParam.class.getSimpleName());
            Map<String, String> arguments = new LinkedHashMap<>();
            String value = this.value();
            if (!StringUtils.hasText(value)) {
                value = this.name();
            }
            if (StringUtils.hasText(value)) {
                arguments.put("name", MessageFormat.format("\"{0}\"", value));
            }
            //注解位置参数
            List<String> positionArguments = new LinkedList<>(arguments.values());
            annotation.setNamedArguments(arguments)
                    .setPositionArguments(positionArguments);
            return annotation;
        }

        @Override
        public String toComment(Parameter annotationOwner) {

            return MessageFormat.format("属性：{0}是一个 rqeust param", annotationOwner.getName());
        }
    }
}
