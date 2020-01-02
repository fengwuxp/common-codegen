package com.wuxp.codegen.spring.annotations;


import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import org.springframework.util.StringUtils;

import javax.persistence.Column;

/**
 * jpa 注解处理
 *
 * @see Column
 */
public class ColumnProcessor extends AbstractAnnotationProcessor<Column, ColumnProcessor.ColumnMate> {


    @Override
    public ColumnMate process(Column annotation) {
        return this.newProxyMate(annotation, ColumnMate.class);
    }

    public abstract static class ColumnMate implements AnnotationMate<Column>, Column {


        @Override
        public String toComment(Class<?> annotationOwner) {
            return this.name();
        }
    }
}
