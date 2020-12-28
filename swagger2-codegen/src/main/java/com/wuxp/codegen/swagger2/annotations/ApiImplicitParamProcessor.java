package com.wuxp.codegen.swagger2.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

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
        public String toComment(Method annotationOwner) {
            String desc = this.value();
            String description = StringUtils.hasText(desc) ? desc : this.value();
            return String.format("属性名称：%s，属性说明：%s，示例输入：%s", this.name(), description, this.example());
        }

    }
}
