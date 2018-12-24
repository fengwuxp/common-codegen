package com.wuxp.codegen.model.mapping;

import com.wuxp.codegen.model.utils.JavaTypeUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础数据的类型映射
 */
public class BaseTypeMapping<T> implements TypeMapping<Class<?>, T> {

    private Map<Class<?>, T> typeMapping = new HashMap<>();

    //时间类型 希望装换的目标类型
    private T dateToClassTarget;

    public BaseTypeMapping(Map<Class<?>, T> typeMapping) {
        this.typeMapping = typeMapping;
    }

    public BaseTypeMapping(Map<Class<?>, T> typeMapping, T dateToClassTarget) {
        this.typeMapping = typeMapping;
        this.dateToClassTarget = dateToClassTarget;
    }

    @Override
    public T mapping(Class<?>[] classes) {
        if (classes == null) {
            return null;
        }
        Class<?> clazz = (Class<?>) classes[0];
        if (JavaTypeUtil.isDate(clazz) && this.dateToClassTarget != null) {
            return this.dateToClassTarget;
        }
        return this.typeMapping.get(clazz);
    }
}
