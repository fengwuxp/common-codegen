package com.wuxp.codegen.core;


/**
 * 用于等待某个任务直到结束
 *
 * @author wuxp
 */
public interface TaskWaiter {

    /**
     * 如果任务未结束，阻塞线程
     */
    void waitTaskCompleted();
}
