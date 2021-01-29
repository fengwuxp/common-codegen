package com.wuxp.codegen.swagger2.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import com.wuxp.codegen.util.RequestMappingUtils;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

/**
 * swagger2 注解处理
 *
 * @author wuxp
 * @see ApiImplicitParam
 */
public class ApiImplicitParamProcessor extends AbstractAnnotationProcessor<ApiImplicitParam, ApiImplicitParamProcessor.ApiImplicitParamMate> {


    @Override
    public ApiImplicitParamMate process(ApiImplicitParam annotation) {
        return this.newProxyMate(annotation, ApiImplicitParamMate.class);
    }

    public abstract static class ApiImplicitParamMate implements AnnotationMate, ApiImplicitParam {

        @Override
        public String toComment(AnnotatedElement annotationOwner) {
            String description = this.value();
            String defaultValue = defaultValue();
            Optional<RequestParam> requestParam = RequestMappingUtils.findRequestParam(annotationOwner);
            if (requestParam.isPresent() && !StringUtils.hasText(defaultValue)) {
                defaultValue = requestParam.get().defaultValue();
            }
            return String.format("属性名称：%s，属性说明：%s，默认值：%s，示例输入：%s", this.name(), description, defaultValue, this.example());
        }

    }
}
