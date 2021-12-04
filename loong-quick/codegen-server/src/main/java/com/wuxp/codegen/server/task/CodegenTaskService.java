package com.wuxp.codegen.server.task;

import javax.annotation.Nullable;

/**
 * 代码生成任务服务
 *
 * @author wuxp
 */
public interface CodegenTaskService {

    /**
     * 创建代码生成任务
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     * @return 任务id
     */
    String create(String projectName, @Nullable String branch);

    /**
     * 通过任务id获取任务进度信息
     *
     * @param taskId 任务id
     * @return 任务进度信息
     */
    CodegenTaskInfo getTask(String taskId);

    /**
     * 释放任务
     *
     * @param taskId 任务id
     */
    void release(String taskId);

}
