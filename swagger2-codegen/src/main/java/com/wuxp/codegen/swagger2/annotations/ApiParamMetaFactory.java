package com.wuxp.codegen.swagger2.annotations;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;
import java.util.Optional;

import static com.wuxp.codegen.meta.annotations.factories.spring.RequestParamMetaFactory.getRequestAnnotationDesc;

/**
 * swagger2 注解处理
 *
 * @author wuxp
 * @see ApiParam
 */
public class ApiParamMetaFactory extends AbstractAnnotationMetaFactory<ApiParam, ApiParamMetaFactory.ApiPramMeta> {


    @Override
    public ApiPramMeta factory(ApiParam annotation) {
        return this.newProxyMate(annotation, ApiPramMeta.class);
    }

    public abstract static class ApiPramMeta implements AnnotationMate, ApiParam {


        @Override
        public String toComment(AnnotatedElement element) {
            String defaultValue = defaultValue();
            Optional<RequestParam> requestParam = RequestMappingUtils.findRequestParam(element);
            if (requestParam.isPresent() && !StringUtils.hasText(defaultValue)) {
                defaultValue = getRequestAnnotationDesc(defaultValue);
            }
            String name = name();
            if (element instanceof Parameter) {
                name = getParameterName((Parameter) element, name);
            }
            return String.format("属性名称：%s，属性说明：%s，默认值：%s，示例输入：%s", name, value(), defaultValue, this.example());
        }
    }
}
