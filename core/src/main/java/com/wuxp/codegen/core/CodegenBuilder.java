package com.wuxp.codegen.core;


/**
 * 代码生成配置builder
 */
public interface CodegenBuilder<T> {


    /**
     * 构建代码生成器
     *
     * @return
     */
    CodeGenerator buildCodeGenerator();
}
