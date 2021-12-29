package com.wuxp.codegen.meta.annotations.factories.spring;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.annotation.ElementType;
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
public class RequestBodyMetaFactory extends AbstractAnnotationMetaFactory<RequestBody, RequestBodyMetaFactory.RequestBodyMate> {


    @Override
    public RequestBodyMetaFactory.RequestBodyMate factory(RequestBody annotation) {

        return super.newProxyMate(annotation, RequestBodyMetaFactory.RequestBodyMate.class);
    }


    public abstract static class RequestBodyMate implements NamedAnnotationMate, RequestBody {


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
            annotation.setElementType(ElementType.PARAMETER);
            return annotation;
        }

        @Override
        public String toComment(Parameter annotationOwner) {
            return String.format("参数：%s是一个RequestBody, %s", this.getParameterName(annotationOwner), required() ? "必填" : "非必填");
        }
    }
}
