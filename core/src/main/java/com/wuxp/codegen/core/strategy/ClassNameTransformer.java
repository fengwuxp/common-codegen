package com.wuxp.codegen.core.strategy;

/**
 * 类名转换器
 */
public interface ClassNameTransformer {


    String transform(Class<?> clazz);
}
