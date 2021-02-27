package com.wuxp.codegen.server.task;

import com.wuxp.codegen.core.ClientProviderType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

/**
 * 代码生成的文件管理策略
 *
 * @author wuxp
 */
public interface CodegenFileManageStrategy {


    String DEFAULT_MODULE_NAME = "web";

    /**
     * 上传生成的sdk文件
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     * @param moduleName  模块名称
     * @param type        client type
     * @param file     上传的sdk代码文件liu
     */
    void upload(String projectName, String branch, String moduleName, ClientProviderType type, MultipartFile file);

    /**
     * 下载生成好的sdk文件
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     * @param moduleName  模块名称
     * @param type        client type
     * @return sdk代码文件
     */
    Optional<File> download(String projectName, String branch, String moduleName, ClientProviderType type);
}
