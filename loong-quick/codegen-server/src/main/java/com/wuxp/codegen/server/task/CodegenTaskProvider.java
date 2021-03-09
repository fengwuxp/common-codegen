package com.wuxp.codegen.server.task;

import java.util.Optional;

/**
 * 代码生成任务提供者
 *
 * @author wuxp
 */
public interface CodegenTaskProvider {


    /**
     * 创建任务
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     * @return 任务id
     */
    String create(String projectName, String branch);

    /**
     * 通过任务id获取任务进度信息
     *
     * @param taskId 任务id
     * @return 任务进度信息
     */
    Optional<CodegenTaskProgressInfo> getTaskProgress(String taskId);

    /**
     * 释放任务
     *
     * @param taskId 任务id
     */
    void release(String taskId);

}
