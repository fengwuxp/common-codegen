package com.wuxp.codegen.core;


/**
 * 代码生成filter
 * @param <T>
 * @param <P>
 */
public interface CodeGenFilter<T, P> {

    /**
     * 过滤
     * @param data
     * @return
     */
    T filter(P data);
}
