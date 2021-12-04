package com.wuxp.codegen.server.controller;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.server.codegen.SdkCodeDescriptor;
import com.wuxp.codegen.server.codegen.SdkCodeManager;
import com.wuxp.codegen.server.codegen.VcsSdkCodeDescriptor;
import com.wuxp.codegen.server.task.CodegenTaskInfo;
import com.wuxp.codegen.server.task.CodegenTaskService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static com.wuxp.codegen.server.constant.VcsConstants.DEFAULT_MODULE_NAME;
import static com.wuxp.codegen.server.constant.WebApiConstants.WEB_API_V1_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * @author wuxp
 */
@Tag(name = "loong codegen", description = "代码生成 restful 接口")
@Slf4j
@RestController
@RequestMapping(WEB_API_V1_PREFIX + "/loong/")
public class LoongCodegenController {

    private final CodegenTaskService codegenTaskService;

    private final SdkCodeManager sdkCodeManager;

    public LoongCodegenController(CodegenTaskService codegenTaskService, SdkCodeManager sdkCodeManager) {
        this.codegenTaskService = codegenTaskService;
        this.sdkCodeManager = sdkCodeManager;
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
    @PostMapping("/task/{project}")
    public String codegenTask(@PathVariable("project") String project, @RequestParam(value = "branch", required = false) String branch) {
        return codegenTaskService.create(project, branch);
    }

    /**
     * @param taskId 代码生成的任务的任务id
     * @return 任务状态
     */
    @Operation(description = "通过代码任务id获取任务处理状态")
    @GetMapping("/task/{taskId}")
    public HttpEntity<CodegenTaskInfo> getCodegenTaskStatusInfo(@PathVariable("taskId") String taskId) {
        return ResponseEntity.ok(codegenTaskService.getTask(taskId));
    }

    @Operation(description = "上传已经生成的sdk代码")
    @PostMapping(value = "/sdk_code", consumes = {MULTIPART_FORM_DATA_VALUE})
    public void uploadCodegenSdk(@RequestParam(value = "projectName") String projectName,
                                 @RequestParam(value = "branch") String branch,
                                 @RequestParam("type") ClientProviderType type,
                                 @RequestParam(value = "moduleName", required = false, defaultValue = DEFAULT_MODULE_NAME) String moduleName,
                                 @RequestParam(value = "file") MultipartFile file) throws IOException {
        SdkCodeDescriptor descriptor = new VcsSdkCodeDescriptor(projectName, type, branch, moduleName);
        sdkCodeManager.storage(descriptor, file.getInputStream());
    }

    @Operation(description = "下载生成的sdk,1：通过代码任务id和ClientProviderType下载代码生成结果，2：通过项目和分支名称下载")
    @GetMapping(value = "/sdk_code/{taskId}", produces = {APPLICATION_OCTET_STREAM_VALUE})
    @Hidden
    public HttpEntity<InputStreamResource> downloadSdkCodeByTask(@PathVariable(value = "taskId") String taskId,
                                                                 @RequestParam("type") ClientProviderType type) throws IOException {
        CodegenTaskInfo taskInfo = codegenTaskService.getTask(taskId);
        return downloadSdkCode(taskInfo.getProjectName(), taskInfo.getBranch(), null, type);
    }

    @Operation(description = "下载生成的sdk,1：通过代码任务id和ClientProviderType下载代码生成结果，2：通过项目和分支名称下载")
    @GetMapping(value = "/sdk_code", produces = {APPLICATION_OCTET_STREAM_VALUE})
    @Hidden
    public HttpEntity<InputStreamResource> downloadSdkCode(@RequestParam(value = "projectName") String projectName,
                                                           @RequestParam(value = "branch", required = false) String branch,
                                                           @RequestParam(value = "moduleName", required = false) String moduleName,
                                                           @RequestParam("type") ClientProviderType type) throws IOException {
        SdkCodeDescriptor descriptor = new VcsSdkCodeDescriptor(projectName, type, branch, moduleName);
        return writeSdkCode(sdkCodeManager.get(descriptor));
    }

    private HttpEntity<InputStreamResource> writeSdkCode(File file) throws IOException {
        Resource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", resource.getFilename()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(new InputStreamResource(resource.getInputStream()));

    }
}
