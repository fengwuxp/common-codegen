package com.wuxp.codegen.swagger.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.annotations.ApiOperation;

/**
 * swagger 注解处理
 *
 * @see ApiOperation
 */
public class ApiOperationProcessor extends AbstractAnnotationProcessor<ApiOperation, ApiOperationProcessor.ApiOperationMate> {


    @Override
    public ApiOperationMate process(ApiOperation annotation) {

        return this.newProxyMate(annotation, ApiOperationMate.class);
    }


    public static abstract class ApiOperationMate implements AnnotationMate<ApiOperation>, ApiOperation {


        @Override
        public String toComment() {
            return null;
        }
    }
}
