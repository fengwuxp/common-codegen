package com.wuxp.codegen.server.controller;

import com.wuxp.codegen.core.CodegenVersion;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * 该类仅用于 mark open api definition
 *
 * @author wuxp
 */
@OpenAPIDefinition(
        info = @Info(
                title = "codegen server",
                version = CodegenVersion.VERSION,
                description = "通过从代码管理平台（git/svn）中获取源代码进行代码生成"
        ),
        externalDocs = @ExternalDocumentation(description = "codegen-quick-server docs",
                url = "https://github.com/fengwuxp/common-codegen/blob/master/docs/doc.md"
        )
)
public class DocController {
}
