package com.wuxp.codegen.core;


/**
 * @author wxup
 * 代码生成匹配器
 */
@FunctionalInterface
public interface CodeGenMatcher {

    /**
     * 是否匹配
     *
     * @param clazz class data
     * @return <code>是否匹配</code>
     */
    boolean match(Class<?> clazz);
}
