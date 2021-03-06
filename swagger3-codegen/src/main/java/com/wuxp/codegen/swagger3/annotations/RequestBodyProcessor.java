package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * swagger3 注解处理
 *
 * @author wuxp
 * @see RequestBody
 */
public class RequestBodyProcessor extends AbstractAnnotationProcessor<RequestBody, RequestBodyProcessor.OperationMate> {


    @Override
    public OperationMate process(RequestBody annotation) {
        return this.newProxyMate(annotation, OperationMate.class);
    }

    public abstract static class OperationMate implements AnnotationMate, RequestBody {

        @Override
        public String toComment(Field annotationOwner) {
            return this.getDescription();
        }

        @Override
        public String toComment(Method annotationOwner) {
            return this.getDescription();
        }

        private String getDescription() {
            return this.description();
        }
    }
}
