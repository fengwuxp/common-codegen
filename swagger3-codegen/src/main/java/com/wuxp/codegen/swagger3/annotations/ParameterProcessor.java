package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.util.RequestMappingUtils;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


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
        public String toComment(Object annotationOwner) {

            String desc = this.description();
            String name = this.name();
            String description = StringUtils.hasText(desc) ? desc : name;
            Optional<RequestParam> requestParam = RequestMappingUtils.findRequestParam(annotationOwner);
            String defaultValue = "";
            if (requestParam.isPresent()) {
                defaultValue = requestParam.get().defaultValue();
            }
            return String.format("属性名称：%s，属性说明：%s，默认值：%s，示例输入：%s", name, defaultValue, description, this.example());
        }


    }
}
