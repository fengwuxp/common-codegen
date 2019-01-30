package com.wuxp.codegen.dragon.swagger2.annotations;

import com.wuxp.codegen.dragon.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.dragon.annotation.processor.AnnotationMate;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

/**
 * swagger2 注解处理
 *
 * @see Api
 */
public class ApiProcessor extends AbstractAnnotationProcessor<Api, ApiProcessor.ApiMate> {


    @Override
    public ApiMate process(Api annotation) {
        return this.newProxyMate(annotation, ApiMate.class);
    }

    public abstract static class ApiMate implements AnnotationMate<Api>, Api {


        @Override
        public String toComment(Field annotationOwner) {
            String[] tags = this.tags();
            String tag = tags[0];
            if (!StringUtils.hasText(tag)) {
                return this.value();
            }


            return tag;
        }
    }
}
