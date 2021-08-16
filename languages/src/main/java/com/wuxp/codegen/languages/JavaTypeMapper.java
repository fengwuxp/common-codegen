package com.wuxp.codegen.languages;

import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public final class JavaTypeMapper {

    /**
     * 自定义的java类型映射
     */
    private final Map<Class<?>, Class<?>[]> javaTypeMappings;

    public JavaTypeMapper(Map<Class<?>, Class<?>[]> javaTypeMappings) {
        this.javaTypeMappings = javaTypeMappings;
    }

    public Class<?>[] mappingClasses(Class<?>... sources) {
        if (ObjectUtils.isEmpty(sources)) {
            return new Class<?>[0];
        }
        return Arrays.stream(sources)
                .filter(Objects::nonNull)
                .map(this::mapClasses)
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .toArray(Class<?>[]::new);
    }

    private Class<?>[] mapClasses(Class<?> clazz) {
        Class<?>[] classes = javaTypeMappings.get(clazz);
        if (classes == null) {
            return new Class<?>[]{clazz};
        }
        return classes;
    }

}
