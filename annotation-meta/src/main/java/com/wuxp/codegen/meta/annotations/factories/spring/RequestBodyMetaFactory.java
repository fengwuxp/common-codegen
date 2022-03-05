package com.wuxp.codegen.meta.annotations.factories.spring;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Parameter;

/**
 * @author wxup
 * @see RequestBody
 * 处理 RequestBody 注解
 */
public class RequestBodyMetaFactory extends AbstractAnnotationMetaFactory<RequestBody, RequestBodyMetaFactory.RequestBodyMate> {


    @Override
    public RequestBodyMetaFactory.RequestBodyMate factory(RequestBody annotation) {

        return super.newProxyMate(annotation, RequestBodyMetaFactory.RequestBodyMate.class);
    }


    public abstract static class RequestBodyMate extends AbstractAnnotationMate implements AnnotationMate, RequestBody {

        @Override
        public String toComment(Parameter annotationOwner) {
            return String.format("参数：%s是一个RequestBody, %s", this.getParameterName(annotationOwner), required() ? "必填" : "非必填");
        }
    }
}
