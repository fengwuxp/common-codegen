package com.wuxp.codegen.swagger.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.annotations.Api;

/**
 * swagger 注解处理
 *
 * @see Api
 */
public class ApiProcessor extends AbstractAnnotationProcessor<Api, ApiProcessor.ApiMate> {


    @Override
    public ApiMate process(Api annotation) {
        return this.newProxyMate(annotation, ApiMate.class);
    }

    public static abstract class ApiMate implements AnnotationMate<Api>, Api {


        @Override
        public String toComment() {
            String[] tags = this.tags();
            if (tags.length == 0) {
                return this.value();
            }

            return tags[0];
        }
    }
}
