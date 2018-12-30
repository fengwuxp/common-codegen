package com.wuxp.codegen.core;


/**
 * 代码生成filter
 * @param <P>
 */
@FunctionalInterface
public interface CodeGenFilter<P> {

    /**
     * 过滤
     * @param data
     * @return
     */
    boolean filter(P data);
}
