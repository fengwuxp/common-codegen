package com.wuxp.codegen.core.mapping;


import java.lang.reflect.Type;

/**
 * 类型映射
 */
public interface TypeMapping<T> {


    T mapping(Type[] classes);

}
