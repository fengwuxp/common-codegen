package com.wuxp.codegen.core.strategy;


import com.wuxp.codegen.model.CommonCodeGenClassMeta;

/**
 * 合并泛型类型描述策略
 */
public interface CombineTypeDescStrategy {


    /**
     * 合并类型
     *
     * @param codeGenClassMetas
     * @return 不同语言的泛型字符串描述
     */
    String combine(CommonCodeGenClassMeta[] codeGenClassMetas);
}
