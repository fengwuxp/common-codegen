package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import com.wuxp.codegen.util.RequestMappingUtils;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import static com.wuxp.codegen.annotation.processors.spring.RequestParamProcessor.getRequestAnnotationDesc;


/**
 * swagger3 注解处理
 *
 * @author wuxp
 * @see Parameter
 */
public class ParameterProcessor extends AbstractAnnotationProcessor<Parameter, ParameterProcessor.ParameterMate> {


    @Override
    public ParameterMate process(Parameter annotation) {
        return this.newProxyMate(annotation, ParameterMate.class);
    }

    public abstract static class ParameterMate implements AnnotationMate, Parameter {


        @Override
        public String toComment(AnnotatedElement annotationOwner) {
            Optional<RequestParam> requestParam = RequestMappingUtils.findRequestParam(annotationOwner);
            String defaultValue = "";
            if (requestParam.isPresent()) {
                defaultValue = getRequestAnnotationDesc(defaultValue);
            }
            String name = name();
            if (annotationOwner instanceof java.lang.reflect.Parameter) {
                name = getParameterName((java.lang.reflect.Parameter) annotationOwner, name);
            }
            return String.format("属性名称：%s，属性说明：%s，默认值：%s，示例输入：%s", name, defaultValue,  this.description(), this.example());
        }


    }
}
