package com.wuxp.codegen.spring.annotations;


import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import org.springframework.util.StringUtils;

import javax.persistence.Table;

/**
 * jpa 注解处理
 *
 * @see Table
 */
public class TableProcessor extends AbstractAnnotationProcessor<Table, TableProcessor.TableModelMate> {


    @Override
    public TableModelMate process(Table annotation) {
        return this.newProxyMate(annotation, TableModelMate.class);
    }

    public abstract static class TableModelMate implements AnnotationMate<Table>, Table {


        @Override
        public String toComment(Class<?> annotationOwner) {
            String name = this.name();
            if (!StringUtils.hasText(name)) {
                return this.schema();
            }
            return name;
        }
    }
}
