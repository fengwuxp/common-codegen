package com.wuxp.codegen.model.mapping;

import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 处理typescript的类型映射
 */
@Slf4j
public class TypescriptTypeMapping implements TypeMapping<Class<?>, List<TypescriptClassMeta>> {

    /**
     * 基础数据类型映射
     */
    protected static final Map<Class<?>, TypescriptClassMeta> BASE_TYPE_MAPPING = new LinkedHashMap<>();

    static {

        BASE_TYPE_MAPPING.put(Date.class, TypescriptClassMeta.DATE);
        BASE_TYPE_MAPPING.put(Boolean.class, TypescriptClassMeta.BOOLEAN);
        BASE_TYPE_MAPPING.put(String.class, TypescriptClassMeta.STRING);
        BASE_TYPE_MAPPING.put(Number.class, TypescriptClassMeta.NUMBER);
        BASE_TYPE_MAPPING.put(Map.class, TypescriptClassMeta.MAP);
        BASE_TYPE_MAPPING.put(Set.class, TypescriptClassMeta.SET);
        BASE_TYPE_MAPPING.put(List.class, TypescriptClassMeta.ARRAY);
        BASE_TYPE_MAPPING.put(Collection.class, TypescriptClassMeta.ARRAY);
        BASE_TYPE_MAPPING.put(void.class, TypescriptClassMeta.VOID);

    }

    protected TypeMapping<Class<?>, TypescriptClassMeta> baseTypeMapping = new BaseTypeMapping<TypescriptClassMeta>(BASE_TYPE_MAPPING);


    /**
     * @param classes 类型列表，大于一个表示有泛型
     * @return 类型描述字符串代码
     */
    @Override
    public List<TypescriptClassMeta> mapping(Class<?>... classes) {

        if (classes == null || classes.length == 0) {
            return null;
        }

        Class<?> clazz = classes[0];

        //1. 类型转换，如果是简单的java类型，则尝试做装换
        //2. 处理枚举类型
        //3. 循环获取泛型
        //4. 处理复杂的数据类型（自定义的java类）

        List<TypescriptClassMeta> list = new ArrayList<>(4);

        if (JavaTypeUtil.isComplex(clazz)) {
            //TODO 复杂的数据类型

        } else {
            Class<?> upConversionType = this.tryUpConversionType(clazz);
            if (upConversionType != null) {
                list.add(baseTypeMapping.mapping(upConversionType));
            } else if (clazz.isEnum()) {
                //TODO 枚举
            } else if (clazz.isArray()) {
                //数组
                list.addAll(this.mapping(clazz.getComponentType()));
                list.add(TypescriptClassMeta.ARRAY);
            }
        }

        //处理泛型
        list.addAll(Arrays.stream(classes)
                .filter(c -> c != clazz).map(this::mapping)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        return list;

    }

    /**
     * 尝试向上转换类型
     *
     * @param clazz
     * @return
     */
    protected Class<?> tryUpConversionType(Class<?> clazz) {
        if (JavaTypeUtil.isNumber(clazz)) {
            //数值类型
            return Number.class;
        } else if (JavaTypeUtil.isString(clazz)) {
            return String.class;
        } else if (JavaTypeUtil.isBoolean(clazz)) {
            return Boolean.class;
        } else if (JavaTypeUtil.isDate(clazz)) {
            return Date.class;
        } else if (JavaTypeUtil.isVoid(clazz)) {
            return void.class;
        } else if (JavaTypeUtil.isSet(clazz)) {
            return Set.class;
        } else if (JavaTypeUtil.isList(clazz)) {
            return List.class;
        } else if (JavaTypeUtil.isCollection(clazz)) {
            return Collection.class;
        } else if (JavaTypeUtil.isMap(clazz)) {
            return Map.class;
        }
        return null;
    }
}
