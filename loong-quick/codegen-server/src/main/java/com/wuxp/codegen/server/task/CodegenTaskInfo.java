package com.wuxp.codegen.server.task;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.beans.Transient;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务信息
 *
 * @author wuxp
 */
@Slf4j
@Data
public class CodegenTaskInfo implements Serializable {

    private static final long serialVersionUID = -3495142387275328243L;

    /**
     * 任务id
     */
    private final String taskId;

    /**
     * 项目名称
     */
    private final String projectName;

    /**
     * 分支名称
     */
    private final String branch;

    /**
     * 源代码远程仓库代码
     */
    private final String repositoryCode;

    /**
     * 任务状态
     */
    private CodegenTaskStatus status;

    /**
     * 已重试的次数
     */
    private final AtomicInteger retries;

    /**
     * 最后抛出的异常
     */
    private transient Exception lastException;

    /**
     * 任务被复用的次数
     */
    private final AtomicInteger taskReferenceCount;

    public CodegenTaskInfo(String taskId, String projectName, String branch, String repositoryCode) {
        this.taskId = taskId;
        this.projectName = projectName;
        this.branch = branch;
        this.repositoryCode = repositoryCode;
        this.status = CodegenTaskStatus.CLONE_CODE;
        this.retries = new AtomicInteger(0);
        this.taskReferenceCount = new AtomicInteger(0);
    }

    public void increase() {
        this.taskReferenceCount.incrementAndGet();
    }

    public void release() {
        this.taskReferenceCount.decrementAndGet();
    }

    @Transient
    public Exception getLastException() {
        return lastException;
    }

    public String getExceptionCause() {
        return lastException == null ? "" : lastException.getMessage();
    }

    public int getRetries() {
        return retries.get();
    }

    public int getTaskReferenceCount() {
        return taskReferenceCount.get();
    }
}


