package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * swagger3 注解处理
 *
 * @see Operation
 */
public class TagProcessor extends AbstractAnnotationProcessor<Tag, TagProcessor.OperationMate> {


    @Override
    public OperationMate process(Tag annotation) {
        return this.newProxyMate(annotation, OperationMate.class);
    }

    public abstract static class OperationMate implements AnnotationMate<Tag>, Tag {

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
