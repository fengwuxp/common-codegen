package com.wuxp.codegen.server.task;

/**
 * 代码生成任务进度
 *
 * @author wuxp
 */
public enum CodegenTaskStatus {


    /**
     * 下载源代码
     */
    CLONE_CODE,

    /**
     * 代码生成处理中
     */
    CODEGEN_PROCESSING,

    /**
     * 生成成功
     */
    SUCCESS,

    /**
     * 成功失败
     */
    FAILURE

}
