package com.wuxp.codegen.model.mapping;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractTypeMapping<T> implements TypeMapping<Class<?>, Collection<T>> {

    /**
     * 基础数据类型映射
     */
    protected final Map<Class<?>, T> BASE_TYPE_MAPPING = new LinkedHashMap<Class<?>, T>();


    /**
     * 生成枚举的元数据依赖
     * @param clazz
     * @return
     */
    protected Collection<CommonCodeGenClassMeta> generateEnumMeta(Class<?> clazz){

        return null;
    }
}
