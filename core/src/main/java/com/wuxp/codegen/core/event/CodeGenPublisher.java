package com.wuxp.codegen.core.event;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;

/**
 * publish codegen event
 */
public interface CodeGenPublisher<T extends CommonCodeGenClassMeta> {


    /**
     * 发送一次生成事件
     *
     * @param data
     */
    void sendCodeGen(T data);

    /**
     * 发送生成异常事件
     *
     * @param exception
     * @param data
     */
    void sendCodeGenError(Exception exception, T data);

    /**
     * 发送生成完成事件
     */
    void sendCodeGenEnd();
}
