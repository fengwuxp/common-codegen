package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.reflect.Method;


/**
 * swagger3 注解处理
 *
 * @see Operation
 */
public class ApiResponseProcessor extends AbstractAnnotationProcessor<ApiResponse, ApiResponseProcessor.OperationMate> {


    @Override
    public OperationMate process(ApiResponse annotation) {
        return this.newProxyMate(annotation, OperationMate.class);
    }

    public abstract static class OperationMate implements AnnotationMate<ApiResponse>, ApiResponse {


        @Override
        public String toComment(Class<?> annotationOwner) {
            return getDescription();
        }

        @Override
        public String toComment(Method annotationOwner) {
            return getDescription();
        }

        private String getDescription() {
            return this.description();
        }

    }
}
