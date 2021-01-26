package com.wuxp.codegen.core;


/**
 * 代码生成配置builder
 *
 * @author wxup
 */
public interface CodegenBuilder {


    /**
     * 构建代码生成器
     *
     * @return 代码配置生成器
     */
    CodeGenerator buildCodeGenerator();
}
