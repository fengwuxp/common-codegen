package com.wuxp.codegen.swagger.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.annotations.ApiParam;

import java.lang.reflect.Field;

/**
 * swagger 注解处理
 *
 * @see ApiParam
 */
public class ApiParamProcessor extends AbstractAnnotationProcessor<ApiParam, ApiParamProcessor.ApiPramMeta> {


    @Override
    public ApiPramMeta process(ApiParam annotation) {
        return this.newProxyMate(annotation, ApiPramMeta.class);
    }

    public abstract static class ApiPramMeta implements AnnotationMate<ApiParam>, ApiParam {


        @Override
        public String toComment(Field annotationOwner) {
            return this.value();
        }
    }
}
