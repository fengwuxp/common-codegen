package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import io.swagger.v3.oas.annotations.Operation;

import java.lang.reflect.Method;


/**
 * swagger3 注解处理
 *
 * @author wxup
 * @see Operation
 */
public class OperationMetaFactory extends AbstractAnnotationMetaFactory<Operation, OperationMetaFactory.OperationMate> {


    @Override
    public OperationMate factory(Operation annotation) {
        return this.newProxyMate(annotation, OperationMate.class);
    }

    public abstract static class OperationMate implements AnnotationMate, Operation {


        @Override
        public String toComment(Class<?> annotationOwner) {
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
