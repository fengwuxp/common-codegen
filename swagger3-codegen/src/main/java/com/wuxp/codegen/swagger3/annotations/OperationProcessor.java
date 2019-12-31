package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.util.StringUtils;


/**
 * swagger3 注解处理
 *
 * @see Operation
 */
public class OperationProcessor extends AbstractAnnotationProcessor<Operation, OperationProcessor.OperationMate> {


    @Override
    public OperationMate process(Operation annotation) {
        return this.newProxyMate(annotation, OperationMate.class);
    }

    public abstract static class OperationMate implements AnnotationMate<Operation>, Operation {


        @Override
        public String toComment(Class<?> annotationOwner) {

            return null;
        }
    }
}
