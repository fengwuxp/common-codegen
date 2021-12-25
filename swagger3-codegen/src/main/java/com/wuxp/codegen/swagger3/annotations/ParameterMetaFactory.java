package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import static com.wuxp.codegen.meta.annotations.factories.spring.RequestParamMetaFactory.getRequestAnnotationDesc;


/**
 * swagger3 注解处理
 *
 * @author wuxp
 * @see Parameter
 */
public class ParameterMetaFactory extends AbstractAnnotationMetaFactory<Parameter, ParameterMetaFactory.ParameterMate> {


    @Override
    public ParameterMate factory(Parameter annotation) {
        return this.newProxyMate(annotation, ParameterMate.class);
    }

    public abstract static class ParameterMate implements AnnotationMate, Parameter {


        @Override
        public String toComment(AnnotatedElement element) {
            Optional<RequestParam> requestParam = RequestMappingUtils.findRequestParam(element);
            String defaultValue = "";
            if (requestParam.isPresent()) {
                defaultValue = getRequestAnnotationDesc(defaultValue);
            }
            String name = name();
            if (element instanceof java.lang.reflect.Parameter) {
                name = getParameterName((java.lang.reflect.Parameter) element, name);
            }
            return String.format("%s %s，默认值：%s，示例输入：%s", name,  this.description(),defaultValue,   this.example());
        }


    }
}
