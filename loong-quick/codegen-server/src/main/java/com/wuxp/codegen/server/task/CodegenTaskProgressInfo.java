package com.wuxp.codegen.server.task;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 任务进度信息
 *
 * @author wuxp
 */
@Slf4j
@Data
public class CodegenTaskProgressInfo implements Serializable {

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
    private volatile int retries;

    /**
     * 最后抛出的异常
     */
    private Exception lastException;

    /**
     * 任务被复用的次数
     */
    private volatile int taskReferenceCount;

    public CodegenTaskProgressInfo(String taskId, String projectName, String branch, String repositoryCode) {
        this.taskId = taskId;
        this.projectName = projectName;
        this.branch = branch;
        this.repositoryCode = repositoryCode;
        status = CodegenTaskStatus.CLONE_CODE;
        retries = 0;
        taskReferenceCount = 0;
    }

    public synchronized void increase() {
        this.taskReferenceCount++;
    }

    public synchronized void release() {
        this.taskReferenceCount--;
    }
}


