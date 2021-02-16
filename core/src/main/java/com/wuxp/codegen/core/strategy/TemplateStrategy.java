package com.wuxp.codegen.core.strategy;

/**
 * 模板处理策略
 *
 * @author wuxp
 */
public interface TemplateStrategy<T> {


    /**
     * 使用模板构建目标代码
     *
     * @param data 模板中可以引用的数据
     * @throws Exception
     */
    void build(T data) throws Exception;

}
