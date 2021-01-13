package com.wuxp.codegen.swagger2.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import com.wuxp.codegen.util.RequestMappingUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;
import java.util.Optional;

import static com.wuxp.codegen.annotation.processors.spring.RequestParamProcessor.getRequestAnnotationDesc;

/**
 * swagger2 注解处理
 *
 * @author wuxp
 * @see ApiParam
 */
public class ApiParamProcessor extends AbstractAnnotationProcessor<ApiParam, ApiParamProcessor.ApiPramMeta> {


    @Override
    public ApiPramMeta process(ApiParam annotation) {
        return this.newProxyMate(annotation, ApiPramMeta.class);
    }

    public abstract static class ApiPramMeta implements AnnotationMate, ApiParam {


        @Override
        public String toComment(Object annotationOwner) {
            String defaultValue = defaultValue();
            Optional<RequestParam> requestParam = RequestMappingUtils.findRequestParam(annotationOwner);
            if (requestParam.isPresent() && !StringUtils.hasText(defaultValue)) {
                defaultValue = getRequestAnnotationDesc(defaultValue);
            }
            String name = name();
            if (annotationOwner instanceof Parameter) {
                name = getParameterName((Parameter) annotationOwner, name);
            }
            return String.format("属性名称：%s，属性说明：%s，默认值：%s，示例输入：%s", name, value(), defaultValue, this.example());
        }
    }
}
