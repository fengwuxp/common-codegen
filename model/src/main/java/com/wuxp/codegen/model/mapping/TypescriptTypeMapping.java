package com.wuxp.codegen.model.mapping;

import com.wuxp.codegen.model.languages.typescript.TypescriptTypeClassMeta;

import java.util.*;

public class TypescriptTypeMapping implements TypeMapping<Class<?>, String> {

    /**
     * 基础数据类型映射
     */
    protected static final Map<Class<?>, TypescriptTypeClassMeta> BASE_TYPE_MAPPING = new LinkedHashMap<>();

    static {

        BASE_TYPE_MAPPING.put(Date.class, TypescriptTypeClassMeta.DATE);
        BASE_TYPE_MAPPING.put(Boolean.class, TypescriptTypeClassMeta.BOOLEAN);
        BASE_TYPE_MAPPING.put(String.class, TypescriptTypeClassMeta.STRING);
        BASE_TYPE_MAPPING.put(Number.class, TypescriptTypeClassMeta.NUMBER);
        BASE_TYPE_MAPPING.put(Map.class, TypescriptTypeClassMeta.MAP);
        BASE_TYPE_MAPPING.put(Set.class, TypescriptTypeClassMeta.SET);
        BASE_TYPE_MAPPING.put(List.class, TypescriptTypeClassMeta.ARRAY);
        BASE_TYPE_MAPPING.put(Collection.class, TypescriptTypeClassMeta.ARRAY);
        BASE_TYPE_MAPPING.put(void.class, TypescriptTypeClassMeta.VOID);

    }

    protected TypeMapping<Class<?>, TypescriptTypeClassMeta> baseTypeMapping = new BaseTypeMapping<TypescriptTypeClassMeta>(BASE_TYPE_MAPPING);


    /**
     * @param classes 类型列表，大于一个表示有泛型
     * @return 类型描述字符串代码
     */
    @Override
    public String mapping(Class<?>[] classes) {

        if (classes == null || classes.length == 0) {
            return null;
        }

        Class<?> clazz = classes[0];

        //1. 类型装换
        //2. 循环获取泛型
        //3. 处理复杂的数据类型（自定义的java bean）


        return null;

    }
}
