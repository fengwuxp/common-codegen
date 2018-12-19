package com.wuxp.codegen.core.mapping;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础数据的类型映射
 */
public class BaseTypeMapping<T> implements TypeMapping<T> {

    private Map<Type, T> typeMapping = new HashMap<>();

    public BaseTypeMapping(Map<Type, T> typeMapping) {
        this.typeMapping = typeMapping;
    }

    @Override
    public T mapping(Type[] classes) {
        if (classes == null) {
            return null;
        }
        return this.typeMapping.get(classes[0]);
    }
}
