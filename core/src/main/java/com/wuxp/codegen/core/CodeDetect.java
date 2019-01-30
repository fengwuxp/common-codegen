package com.wuxp.codegen.core;

import com.wuxp.codegen.model.languages.java.JavaClassMeta;

/**
 * 代码检查，可以在生成代码的时候顺带检查代码的一下规范（完整性）
 */
@FunctionalInterface
public interface CodeDetect {

    /**
     * 检查一个类是否符合代码编写的规范
     *
     * @param meta
     */
    void detect(JavaClassMeta meta);
}
