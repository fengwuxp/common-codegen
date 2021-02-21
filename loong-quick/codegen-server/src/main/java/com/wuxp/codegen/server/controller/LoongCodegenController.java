package com.wuxp.codegen.server.controller;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.server.task.CodegenTaskProgressInfo;
import com.wuxp.codegen.server.task.CodegenTaskProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author wuxp
 */
@Tag(name = "loong codegen", description = "代码生成restful接口")
@Slf4j
@RestController
@RequestMapping("/loong")
public class LoongCodegenController {


    private final CodegenTaskProvider codegenTaskProvider;

    public LoongCodegenController(CodegenTaskProvider codegenTaskProvider) {
        this.codegenTaskProvider = codegenTaskProvider;
    }

    /**
     * 创建一个代码生成的任务
     * <>
     * 如果多个请求同时执行一个分支的代码，将会复用该任务
     * </>
     *
     * @param project 项目名称
     * @param branch  项目分支名称
     * @return 返回一个全局唯一任务标识
     */
    @Operation(description = "创建一个代码生成的任务，返回一个全局唯一任务标识")
    @PostMapping("/{project}/{branch}")
    public String codegenTask(@PathVariable("project") String project, @PathVariable("branch") String branch) {

        return codegenTaskProvider.create(project, branch);
    }

    /**
     * @param taskId 代码生成的任务的任务id
     * @return 任务状态
     */
    @Operation(description = "通过代码任务id获取任务状态")
    @PostMapping("/codegen/task")
    public HttpEntity<CodegenTaskProgressInfo> getCodegenTaskStatusInfo(@RequestParam("taskId") String taskId) {
        return ResponseEntity.of(codegenTaskProvider.getTaskProgress(taskId));
    }

    @Operation(description = "通过代码任务id和ClientProviderType下载代码生成结果")
    @PostMapping("/codegen/download")
    public void downloadTaskResult(@RequestParam("taskId") String taskId, @RequestParam("type") ClientProviderType type) {

    }
}
