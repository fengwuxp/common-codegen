package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;


/**
 * swagger3 注解处理
 *
 * @author wuxp
 * @see Tag
 */
public class TagMetaFactory extends AbstractAnnotationMetaFactory<Tag, TagMetaFactory.TagMate> {


    @Override
    public TagMate factory(Tag annotation) {
        return this.newProxyMate(annotation, TagMate.class);
    }

    public abstract static class TagMate implements AnnotationMate, Tag {

        @Override
        public String toComment(Class<?> annotationOwner) {
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
