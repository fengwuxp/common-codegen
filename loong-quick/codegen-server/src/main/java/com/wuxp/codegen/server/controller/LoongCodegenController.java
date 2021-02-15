package com.wuxp.codegen.server.controller;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuxp
 */
@OpenAPIDefinition(
        info = @Info(
                title = "sdk codegen 平台",
                version = "1.0.0",
                description = "通过从代码管理平台（git/svn）中获取源代码进行代码生成"
        ),
        externalDocs = @ExternalDocumentation(description = "代码生成文档",
                url = "https://github.com/fengwuxp/common-codegen/blob/master/docs/doc.md"
        )
)
@Tag(name = "loong codegen", description = "代码生成restful接口")
@Slf4j
@RestController
@RequestMapping("/loong")
public class LoongCodegenController {

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

        return null;
    }
}
