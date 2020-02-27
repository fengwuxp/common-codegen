package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * swagger3 注解处理
 *
 * @see Schema
 */
public class SchemaProcessor extends AbstractAnnotationProcessor<Schema, SchemaProcessor.OperationMate> {


    @Override
    public OperationMate process(Schema annotation) {
        return this.newProxyMate(annotation, OperationMate.class);
    }

    public abstract static class OperationMate implements AnnotationMate<Schema>, Schema {


        @Override
        public String toComment(Class<?> annotationOwner) {
            return this.getDescription();
        }

        @Override
        public String toComment(Field annotationOwner) {
            return this.getDescription();
        }

        @Override
        public String toComment(Method annotationOwner) {
            return this.getDescription();
        }

        private String getDescription() {
            String description = this.description();
            if (!StringUtils.hasText(description())) {
                return this.name();
            }
            return description;
        }
    }
}
