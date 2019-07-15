package com.wuxp.codegen.core;


import com.wuxp.codegen.core.config.CodegenGlobalConfig;

/**
 * 代码生成配置builder
 */
public interface CodegenBuilder<T> {


    CodegenGlobalConfig CODEGEN_GLOBAL_CONFIG = CodegenGlobalConfig.builder().build();



    /**
     * 构建代码生成器
     *
     * @return
     */
    CodeGenerator buildCodeGenerator();
}
