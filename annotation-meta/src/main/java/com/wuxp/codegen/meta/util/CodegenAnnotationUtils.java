package com.wuxp.codegen.meta.util;

import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.*;

public final class CodegenAnnotationUtils {

    private CodegenAnnotationUtils() {
        throw new AssertionError();
    }

    public static boolean matchAnnotationsAttribute(Annotation[] annotations, String name, Object value) {
        Assert.notNull(annotations, "annotations must not null");
        return Arrays.stream(annotations)
                .anyMatch(annotation -> matchAnnotationAttribute(annotation, name, value));
    }

    public static boolean matchAnnotationAttribute(Annotation annotation, String name, Object value) {
        Assert.notNull(annotation, "annotation must not null");
        return Objects.equals(value, getAnnotationAttributes(annotation).get(name));
    }

    public static Map<String, Object> getAnnotationAttributes(Annotation annotation) {
        Assert.notNull(annotation, "annotation must not null");
        return AnnotationUtils.getAnnotationAttributes(annotation, false);
    }

    public static Map<String, Object> getDefaultAnnotationAttributes(Class<? extends Annotation> type) {
        return MergedAnnotation.of(type).asMap();
    }

    @SafeVarargs
    public static boolean existAnnotations(Annotation[] annotations, Class<? extends Annotation>... types) {
        return Arrays.stream(types)
                .map(tClass -> findAnnotations(annotations, tClass))
                .anyMatch(Optional::isPresent);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Annotation> Optional<T> findAnnotations(Annotation[] annotations, Class<T> type) {
        if (annotations == null) {
            return Optional.empty();
        }
        return Arrays.stream(annotations)
                .filter(annotation -> type.equals(annotation.annotationType()))
                .map(annotation -> (T) annotation)
                .findFirst();
    }

    public static Map<String, String> getAndQuoteStringTypeAttributes(Annotation annotation) {
        Map<String, Object> defaultAttributes = CodegenAnnotationUtils.getDefaultAnnotationAttributes(annotation.annotationType());
        Map<String, Object> attributes = getAnnotationAttributes(annotation);
        Map<String, String> result = new HashMap<>();
        attributes.forEach((key, val) -> {
            if (val == null) {
                return;
            }
            if (Objects.equals(val, defaultAttributes.get(key))) {
                return;
            }
            if (JavaTypeUtils.isString(val.getClass())) {
                result.put(key, quote((String) val));
                return;
            }
            if (JavaTypeUtils.isJavaBaseType(val.getClass())) {
                result.put(key, String.valueOf(val));
            }
        });
        return Collections.unmodifiableMap(result);
    }

    @Nullable
    public static String quote(String text) {
        if (StringUtils.hasText(text)) {
            return String.format("\"%s\"", text);
        }
        return null;
    }
}
