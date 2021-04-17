package com.wuxp.codegen.annotation.processors.spring;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.annotation.processors.NamedAnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.wuxp.codegen.annotation.processors.spring.RequestParamMetaFactory.getRequestAnnotationDesc;

/**
 * @author wxup
 * @see RequestHeader
 * 处理 RequestHeader 注解
 */
public class RequestHeaderMetaFactory extends AbstractAnnotationMetaFactory<RequestHeader, RequestHeaderMetaFactory.RequestHeaderMate> {


    @Override
    public RequestHeaderMetaFactory.RequestHeaderMate factory(RequestHeader annotation) {

        return super.newProxyMate(annotation, RequestHeaderMetaFactory.RequestHeaderMate.class);
    }


    public abstract static class RequestHeaderMate implements NamedAnnotationMate, RequestHeader {

        protected final RequestHeader requestHeader;


        protected RequestHeaderMate(RequestHeader requestHeader) {
            this.requestHeader = requestHeader;
        }

        @Override
        public String name() {
            return requestHeader.name();
        }

        @Override
        public String value() {
            return requestHeader.value();
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(RequestHeader.class.getSimpleName());
            Map<String, String> arguments = new LinkedHashMap<>();
            String value = getParameterName(annotationOwner);
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
            return getRequestAnnotationDesc(String.format("参数：%s是一个请求头，%s",this.getParameterName(annotationOwner), required() ? "必填" : "非必填"), defaultValue());
        }
    }
}
