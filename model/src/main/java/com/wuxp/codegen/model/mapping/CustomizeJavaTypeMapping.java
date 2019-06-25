package com.wuxp.codegen.model.mapping;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 自定义的java type映射
 */
@Slf4j
public class CustomizeJavaTypeMapping implements TypeMapping<Class<?>, List<Class<?>>> {


    private Map<Class<?>, Class<?>[]> classMap;

    public CustomizeJavaTypeMapping(Map<Class<?>, Class<?>[]> classMap) {
        this.classMap = classMap;
    }

    @Override
    public List<Class<?>> mapping(Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return null;
        }

        return Arrays.stream(classes)
                .map(clazz -> {
                    Class<?>[] list = this.classMap.get(clazz);
                    if (list == null) {
                        return new Class<?>[]{clazz};
                    }
                    return list;
                })
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }
}
