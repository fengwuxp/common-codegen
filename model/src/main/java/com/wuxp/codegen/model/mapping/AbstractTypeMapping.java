package com.wuxp.codegen.model.mapping;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTypeMapping<T> implements TypeMapping<Class<?>, List<T>> {

    /**
     * 基础数据类型映射
     */
    public static final Map<Class<?>,CommonCodeGenClassMeta> BASE_TYPE_MAPPING = new LinkedHashMap<>();

    /**
     * 自定义的类型映射
     */
    public static final Map<Class<?>,Class<?>[]> CUSTOMIZE_TYPE_MAPPING = new LinkedHashMap<>();


}
