package com.wuxp.codegen.meta.annotations.factories.spring;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;

/**
 * @author wxup
 * @see RequestParam
 * 处理 RequestParam 注解
 */
public class RequestParamMetaFactory extends AbstractAnnotationMetaFactory<RequestParam, RequestParamMetaFactory.RequestParamMate> {

    @Override
    public RequestParamMetaFactory.RequestParamMate factory(RequestParam annotation) {

        return super.newProxyMate(annotation, RequestParamMetaFactory.RequestParamMate.class);
    }

    public abstract static class RequestParamMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, RequestParam {
        @Override
        public String toComment(Parameter annotationOwner) {
            return String.format("参数：%s 是一个查询参数或表单参数，%s，默认值：%s", this.getParameterName(annotationOwner),
                    required() ? "必填" : "非必填",
                    getAttributeSafeToString(ANNOTATION_DEFAULT_VALUE_KEY));
        }
    }

}
