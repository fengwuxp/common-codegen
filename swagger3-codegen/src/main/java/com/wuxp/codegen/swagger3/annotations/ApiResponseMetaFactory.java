package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.reflect.Method;


/**
 * swagger3 注解处理
 *
 * @author wuxp
 * @see ApiResponse
 */
public class ApiResponseMetaFactory extends AbstractAnnotationMetaFactory<ApiResponse, ApiResponseMetaFactory.ApiResponseMate> {


    @Override
    public ApiResponseMate factory(ApiResponse annotation) {
        return this.newProxyMate(annotation, ApiResponseMate.class);
    }

    public abstract static class ApiResponseMate implements AnnotationMate, ApiResponse {


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
