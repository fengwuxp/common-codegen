package com.wuxp.codegen.core;

public interface CodeGenClassSupportHandler {

    /**
     * 提前调用，用于判断是否需要执行对应的处理
     *
     * @param sourceType 数据类类型
     * @return 是否支持处理
     */
    boolean supports(Class<?> sourceType);
}
