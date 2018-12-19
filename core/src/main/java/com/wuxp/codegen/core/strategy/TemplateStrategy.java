package com.wuxp.codegen.core.strategy;

/**
 * 模板策略
 */
public interface TemplateStrategy<T> {


    /**
     * 使用模板构建目标代码
     *
     * @param data
     */
    void build(T data);

}
