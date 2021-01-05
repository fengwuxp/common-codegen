package com.wuxp.codegen.util;

import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
        boolean patternIsEmpty = patterns == null || patterns.length == 0;
        boolean otherIsEmpty = otherPatterns == null || otherPatterns.length == 0;
        if (patternIsEmpty && otherIsEmpty) {
            return null;
        }
        if (patternIsEmpty) {
            return otherPatterns[0];
        }
        if (otherIsEmpty) {
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


    public static Optional<PathVariable> findPathVariable(Annotation[] annotations) {
        return findAnnotations(annotations, PathVariable.class);
    }

    public static Optional<RequestParam> findRequestParam(Object annotationOwner) {
        if (annotationOwner instanceof Parameter) {
            return findRequestParam(((Parameter) annotationOwner).getAnnotations());
        }
        return Optional.empty();

    }

    public static Optional<RequestParam> findRequestParam(Annotation[] annotations) {
        return findAnnotations(annotations, RequestParam.class);
    }

    private static <T> Optional<T> findAnnotations(Annotation[] annotations, Class<T> annotationType) {
        return Arrays.stream(annotations)
                .filter(annotation -> annotationType.equals(annotation.annotationType()))
                .map(annotation -> (T) annotation)
                .findFirst();
    }
}
