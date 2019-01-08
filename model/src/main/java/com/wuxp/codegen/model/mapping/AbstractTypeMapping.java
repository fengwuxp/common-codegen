package com.wuxp.codegen.model.mapping;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractTypeMapping<T> implements TypeMapping<Class<?>, Collection<T>> {

    /**
     * 基础数据类型映射
     */
    public static final Map<Class<?>,CommonCodeGenClassMeta> BASE_TYPE_MAPPING = new LinkedHashMap<>();


}
