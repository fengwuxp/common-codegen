package com.wuxp.codegen.core.strategy;

/**
 * 生成文件输出的策略
 */
public interface FileOutPutStrategy<T> {


    /**
     * 生成输出路径
     *
     * @param data
     * @return
     */
    String genOutPath(T data);
}
