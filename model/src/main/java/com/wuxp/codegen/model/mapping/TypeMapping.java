package com.wuxp.codegen.model.mapping;


import java.lang.reflect.Type;

/**
 * 类型映射
 *
 * @author wxup
 */
public interface TypeMapping<C extends Type, T> {


    /**
     * <p>
     * 类型映射
     * </p>
     *
     * @param classes 类型列表，大于一个表示有泛型
     * @return
     */
    T mapping(C... classes);

}
