package com.wuxp.codegen.processor.spring;

import com.wuxp.codegen.processor.AnnotationProcessor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;

/**
 * 处理spring mvc的RequestMapping相关注解
 */
public class RequestMappingProcessor implements AnnotationProcessor<RequestMappingProcessor.RequestMappingMate> {

    @Override
    public RequestMappingMate process(Annotation annotation) {

        if (annotation instanceof RequestMapping) {

            RequestMapping mapping = (RequestMapping) annotation;
            return RequestMappingMate.builder()
                    .value(mapping.value())
                    .name(mapping.name())
                    .consumes(mapping.consumes())
                    .produces(mapping.produces())
                    .method(mapping.method())
                    .headers(mapping.headers())
                    .params(mapping.params())
                    .path(mapping.path())
                    .build();
        } else if (annotation instanceof GetMapping) {

            GetMapping mapping = (GetMapping) annotation;
            return RequestMappingMate.builder()
                    .value(mapping.value())
                    .name(mapping.name())
                    .consumes(mapping.consumes())
                    .produces(mapping.produces())
                    .method(new RequestMethod[]{RequestMethod.GET})
                    .headers(mapping.headers())
                    .params(mapping.params())
                    .path(mapping.path())
                    .build();
        } else if (annotation instanceof PostMapping) {

            PostMapping mapping = (PostMapping) annotation;
            return RequestMappingMate.builder()
                    .value(mapping.value())
                    .name(mapping.name())
                    .consumes(mapping.consumes())
                    .produces(mapping.produces())
                    .method(new RequestMethod[]{RequestMethod.POST})
                    .headers(mapping.headers())
                    .params(mapping.params())
                    .path(mapping.path())
                    .build();
        } else if (annotation instanceof DeleteMapping) {

            DeleteMapping mapping = (DeleteMapping) annotation;
            return RequestMappingMate.builder()
                    .value(mapping.value())
                    .name(mapping.name())
                    .consumes(mapping.consumes())
                    .produces(mapping.produces())
                    .method(new RequestMethod[]{RequestMethod.DELETE})
                    .headers(mapping.headers())
                    .params(mapping.params())
                    .path(mapping.path())
                    .build();
        } else if (annotation instanceof PutMapping) {

            PutMapping mapping = (PutMapping) annotation;
            return RequestMappingMate.builder()
                    .value(mapping.value())
                    .name(mapping.name())
                    .consumes(mapping.consumes())
                    .produces(mapping.produces())
                    .method(new RequestMethod[]{RequestMethod.PUT})
                    .headers(mapping.headers())
                    .params(mapping.params())
                    .path(mapping.path())
                    .build();
        } else if (annotation instanceof PatchMapping) {

            PatchMapping mapping = (PatchMapping) annotation;
            return RequestMappingMate.builder()
                    .value(mapping.value())
                    .name(mapping.name())
                    .consumes(mapping.consumes())
                    .produces(mapping.produces())
                    .method(new RequestMethod[]{RequestMethod.PATCH})
                    .headers(mapping.headers())
                    .params(mapping.params())
                    .path(mapping.path())
                    .build();
        }

        return null;
    }


    @Builder
    @Data
    public static class RequestMappingMate {

        /**
         * 请求路径
         * {@link RequestMapping#name}.
         */
        String name;

        /**
         * {@link RequestMapping#value}.
         */
        String[] value;

        /**
         * {@link RequestMapping#path}.
         */
        String[] path;

        /**
         * {@link RequestMapping#method}.
         */
        RequestMethod[] method;

        /**
         * {@link RequestMapping#params}.
         */
        String[] params;


        /**
         * {@link RequestMapping#headers}.
         */
        String[] headers;


        /**
         * {@link RequestMapping#consumes}.
         */
        String[] consumes;


        /**
         * {@link RequestMapping#produces}.
         */
        String[] produces;
    }
}
