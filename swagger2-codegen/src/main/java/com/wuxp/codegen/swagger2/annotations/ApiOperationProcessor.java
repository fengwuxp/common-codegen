package com.wuxp.codegen.swagger2.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.Field;

/**
 * swagger2 注解处理
 *
 * @see ApiOperation
 */
public class ApiOperationProcessor extends AbstractAnnotationProcessor<ApiOperation, ApiOperationProcessor.ApiOperationMeta> {


    @Override
    public ApiOperationMeta process(ApiOperation annotation) {
        return this.newProxyMate(annotation, ApiOperationMeta.class);
    }

    public abstract static class ApiOperationMeta implements AnnotationMate<ApiOperation>, ApiOperation {


        @Override
        public String toComment(Field annotationOwner) {
            return this.value();
        }
    }
}
