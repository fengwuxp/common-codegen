package com.wuxp.codegen.core;


/**
 * @author wxup
 * 代码生成匹配器
 */
@FunctionalInterface
public interface CodeGenMatcher {

    boolean match(Class<?> clazz);
}
