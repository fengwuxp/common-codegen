package com.wuxp.codegen.swagger2.annotations;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * swagger2 注解处理
 *
 * @author wuxp
 * @see Api
 */
public class ApiMetaFactory extends AbstractAnnotationMetaFactory<Api, ApiMetaFactory.ApiMate> {


    @Override
    public ApiMate factory(Api annotation) {
        return this.newProxyMate(annotation, ApiMate.class);
    }

    public abstract static class ApiMate implements AnnotationMate, Api {


        @Override
        public String toComment(Class<?> annotationOwner) {
            return getComment();
        }

        @Override
        public String toComment(Field annotationOwner) {
            return getComment();
        }

        private String getComment() {
            String[] tags = this.tags();
            String tag = tags[0];
            if (!StringUtils.hasText(tag)) {
                return this.value();
            }
            return tag;
        }
    }
}
