package com.wuxp.codegen.meta.annotations.factories.spring;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.reflect.Parameter;

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


    public abstract static class RequestHeaderMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, RequestHeader {

        @Override
        public String toComment(Parameter annotationOwner) {
            return String.format("参数：%s 是一个请求头，%s，默认值：%s", this.getParameterName(annotationOwner),
                    required() ? "必填" : "非必填",
                    getAttributeSafeToString(ANNOTATION_DEFAULT_VALUE_KEY));
        }
    }
}
