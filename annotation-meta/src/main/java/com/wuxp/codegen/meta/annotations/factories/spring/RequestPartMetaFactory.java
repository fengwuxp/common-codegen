package com.wuxp.codegen.meta.annotations.factories.spring;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.bind.annotation.RequestPart;

import java.lang.reflect.Parameter;

/**
 * @author wxup
 * @see RequestPart
 * 处理 RequestPart 注解
 */
public class RequestPartMetaFactory extends AbstractAnnotationMetaFactory<RequestPart, RequestPartMetaFactory.RequestPartMate> {


    @Override
    public RequestPartMetaFactory.RequestPartMate factory(RequestPart annotation) {

        return super.newProxyMate(annotation, RequestPartMetaFactory.RequestPartMate.class);
    }


    public abstract static class RequestPartMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, RequestPart {

        @Override
        public String toComment(Parameter annotationOwner) {
            return String.format("参数：%s是一个文件对象, %s", this.getParameterName(annotationOwner), required() ? "必填" : "非必填");
        }
    }
}
