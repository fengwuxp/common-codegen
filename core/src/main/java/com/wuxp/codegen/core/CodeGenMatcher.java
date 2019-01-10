package com.wuxp.codegen.core;


/**
 * 代码生成匹配器
 */
@FunctionalInterface
public interface CodeGenMatcher {

    boolean match(Class<?> clazz);
}
