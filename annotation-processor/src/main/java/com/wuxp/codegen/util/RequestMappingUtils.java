package com.wuxp.codegen.util;

import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wuxp
 */
public final class RequestMappingUtils {

    private final static PathMatcher PATH_MATCHER = new AntPathMatcher();

    private final static RequestMappingProcessor REQUEST_MAPPING_PROCESSOR = new RequestMappingProcessor();


    public static String combinePath(RequestMappingProcessor.RequestMappingMate annotationMate, Method annotationOwner) {
        Class<?> declaringClass = annotationOwner.getDeclaringClass();
        Optional<RequestMappingProcessor.RequestMappingMate> requestAnnotation = findRequestMappingAnnotation(declaringClass.getAnnotations());
        String[] clazzMappingValues = requestAnnotation.map(RequestMapping::value).orElse(null);
        String[] v2 = annotationMate.value();
        return combinePath(clazzMappingValues, v2);
    }

    /**
     * 合并url
     *
     * @param patterns      类上的{@link org.springframework.web.bind.annotation.RequestMapping}注解的 value
     * @param otherPatterns 方法上的{@link org.springframework.web.bind.annotation.RequestMapping}注解的 value
     * @return 合并后的请url，可能会返回null
     */
    public static String combinePath(String[] patterns, String[] otherPatterns) {
        if (patterns == null || patterns.length == 0) {
            return null;
        }
        if (otherPatterns == null || otherPatterns.length == 0) {
            return patterns[0];
        }
        for (String pattern1 : patterns) {
            for (String pattern2 : otherPatterns) {
                return PATH_MATCHER.combine(pattern1, pattern2);
            }
        }
        return null;
    }

    public static Optional<RequestMappingProcessor.RequestMappingMate> findRequestMappingAnnotation(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .map(annotation -> {
                    try {
                        return REQUEST_MAPPING_PROCESSOR.process(annotation);
                    } catch (Exception ignore) {
                    }
                    return null;
                }).filter(Objects::nonNull)
                .findFirst();
    }
}