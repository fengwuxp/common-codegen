package com.wuxp.codegen.core.processor;


/**
 * 解释处理器
 *
 * @param <I> 输入
 * @param <O> 输出
 */
public interface GenericParserProcessor<I, O> {

    O processor(I input);
}
