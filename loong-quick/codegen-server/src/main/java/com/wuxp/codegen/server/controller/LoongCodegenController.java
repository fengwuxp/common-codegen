package com.wuxp.codegen.server.controller;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.server.task.CodegenFileManageStrategy;
import com.wuxp.codegen.server.task.CodegenTaskProgressInfo;
import com.wuxp.codegen.server.task.CodegenTaskProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.wuxp.codegen.server.task.CodegenFileManageStrategy.DEFAULT_MODULE_NAME;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * @author wuxp
 */
@Tag(name = "loong codegen", description = "代码生成restful接口")
@Slf4j
@RestController
@RequestMapping("/loong")
public class LoongCodegenController {


    private final CodegenTaskProvider codegenTaskProvider;

    private final CodegenFileManageStrategy codegenFileManageStrategy;

    public LoongCodegenController(CodegenTaskProvider codegenTaskProvider, CodegenFileManageStrategy codegenFileManageStrategy) {
        this.codegenTaskProvider = codegenTaskProvider;
        this.codegenFileManageStrategy = codegenFileManageStrategy;
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
    @PostMapping("/task/{project}/{branch}")
    public String codegenTask(@PathVariable("project") String project, @PathVariable("branch") String branch) {
        return codegenTaskProvider.create(project, branch);
    }

    /**
     * @param taskId 代码生成的任务的任务id
     * @return 任务状态
     */
    @Operation(description = "通过代码任务id获取任务处理状态")
    @PostMapping("/task/{task}/progress}")
    public HttpEntity<CodegenTaskProgressInfo> getCodegenTaskStatusInfo(@RequestParam("taskId") String taskId) {
        return ResponseEntity.of(codegenTaskProvider.getTaskProgress(taskId));
    }

    @Operation(description = "上传已经生成的sdk代码")
    @PostMapping(value = "/sdk_code", consumes = {MULTIPART_FORM_DATA_VALUE})
    public void uploadCodegenSdk(@RequestParam(value = "projectName") String projectName,
                                 @RequestParam(value = "branch") String branch,
                                 @RequestParam("type") ClientProviderType type,
                                 @RequestParam(value = "moduleName", required = false, defaultValue = DEFAULT_MODULE_NAME) String moduleName,
                                 @RequestParam(value = "file") MultipartFile file) {
        codegenFileManageStrategy.upload(projectName, branch, moduleName, type, file);
    }

    @Operation(description = "下载生成的sdk,1：通过代码任务id和ClientProviderType下载代码生成结果，2：通过项目和分支名称下载")
    @GetMapping(value = "/sdk_code", produces = {APPLICATION_OCTET_STREAM_VALUE})
    public HttpEntity<InputStreamResource> downloadTaskResult(@RequestParam(value = "taskId", required = false) String taskId,
                                                              @RequestParam("type") ClientProviderType type,
                                                              @RequestParam(value = "projectName", required = false) String projectName,
                                                              @RequestParam(value = "branch", required = false) String branch,
                                                              @RequestParam(value = "moduleName", required = false) String moduleName) throws IOException {
        if (StringUtils.hasText(taskId)) {
            Optional<File> optionalFile = codegenTaskProvider.getTaskProgress(taskId)
                    .map(progressInfo -> codegenFileManageStrategy.download(progressInfo.getProjectName(), progressInfo.getBranch(), moduleName, type))
                    .filter(Optional::isPresent)
                    .map(Optional::get);
            return downloadResult(optionalFile);
        }
        Optional<File> optionalFile = codegenFileManageStrategy.download(projectName, branch, moduleName, type);
        return downloadResult(optionalFile);

    }

    private HttpEntity<InputStreamResource> downloadResult(Optional<File> optional) throws IOException {
        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        FileSystemResource downloadResource = new FileSystemResource(optional.get());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadResource.getFilename()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(downloadResource.contentLength())
                .body(new InputStreamResource(downloadResource.getInputStream()));

    }
}
