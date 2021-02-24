package com.wuxp.codegen.server.task;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.server.plugins.CodegenPluginExecuteStrategy;
import com.wuxp.codegen.server.vcs.SourcecodeRepository;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * 基于zip的文件管理
 *
 * @author wuxp
 */
@Slf4j
public class ZipCodegenFileManageStrategy implements CodegenFileManageStrategy {

    private static final String OUTPUT_DIR = String.join(File.separator, "target", "codegen-sdk", "loong");

    private final SourcecodeRepository sourcecodeRepository;

    private final CodegenPluginExecuteStrategy codegenPluginExecuteStrategy;

    private final String uploadTempDir;

    public ZipCodegenFileManageStrategy(SourcecodeRepository sourcecodeRepository, CodegenPluginExecuteStrategy codegenPluginExecuteStrategy, String uploadTempDir) {
        this.sourcecodeRepository = sourcecodeRepository;
        this.codegenPluginExecuteStrategy = codegenPluginExecuteStrategy;
        if (!StringUtils.hasText(uploadTempDir)) {
            uploadTempDir = System.getProperty("java.io.tmpdir");
        }
        Assert.notNull(uploadTempDir, "上传文件保存的临时目录不能为空");
        if (uploadTempDir.endsWith(File.separator)) {
            uploadTempDir = uploadTempDir.substring(0, uploadTempDir.length() - 1);
        }
        File file = new File(uploadTempDir);
        if (!file.exists()) {
            file.mkdir();
        }
        this.uploadTempDir = uploadTempDir;
    }

    @Override
    public void upload(String projectName, String branch, String moduleName, ClientProviderType type, MultipartFile sdkFile) {
        moduleName = getModuleNameOrDefault(moduleName);
        branch = getBranchOrDefault(branch);
        String tempSdkFilepath = this.getTempSdkFilepath(projectName, branch, moduleName, type);
        if (log.isInfoEnabled()) {
            log.info("上传项目：{}，分支：{}，的{}sdk到：{}", projectName, branch, type, tempSdkFilepath);
        }
        File file = new File(tempSdkFilepath);
        file.deleteOnExit();
        try {
            file.createNewFile();
            nioTransferCopy(sdkFile, file);
        } catch (IOException exception) {
            log.error("上传sdk文件异常：{}", exception.getMessage(), exception);
        }

    }

    @Override
    public File download(String projectName, String branch, String moduleName, ClientProviderType type) {
        moduleName = getModuleNameOrDefault(moduleName);
        branch = getBranchOrDefault(branch);
        String tempSdkFilepath = this.getTempSdkFilepath(projectName, branch, moduleName, type);
        File tempSdkFile = new File(tempSdkFilepath);
        // 存在已上传的sdk文件
        if (tempSdkFile.exists()) {
            return tempSdkFile;
        }
        // 不存在，从本地仓库中获取
        File sdk = getSdkCodegenFile(projectName, branch, type, moduleName);
        try {
            ZipFile zipFile = new ZipFile(String.format("%s.zip", type.name().toLowerCase()));
            zipFile.addFolder(sdk);
            return zipFile.getFile();
        } catch (ZipException exception) {
            log.error("zip压缩sdk文件失败，sdk文件路径：{}，message：{}", sdk.getAbsolutePath(), exception.getMessage(), exception);
            throw new CodegenTaskException(exception);
        }
    }

    private File getSdkCodegenFile(String projectName, String branch, ClientProviderType type, String moduleName) {
        String localDirectory = sourcecodeRepository.getLocalDirectory(projectName, branch);
        List<String> moduleFiles = codegenPluginExecuteStrategy.findModuleFiles(localDirectory, moduleName);
        if (moduleFiles.isEmpty()) {
            throw new CodegenTaskException("未找到匹配本地仓库模块目录");
        }
        if (moduleFiles.size() > 1) {
            throw new CodegenTaskException("找到了多个匹配的模块");
        }
        String filepath = moduleFiles.get(0);
        String modelCodegenDir = String.join(File.separator, filepath.substring(0, filepath.lastIndexOf(".")), OUTPUT_DIR);

        File codegenDir = new File(String.join(File.separator, modelCodegenDir, "swagger_2"));
        if (!codegenDir.exists()) {
            codegenDir = new File(String.join(File.separator, modelCodegenDir, "swagger_3"));
        }
        if (!codegenDir.exists()) {
            throw new CodegenTaskException("未存在sdk代码生成的目录");
        }

        File sdk = new File(String.join(File.separator, codegenDir.getAbsolutePath(), type.name().toLowerCase()));
        if (!sdk.exists()) {
            throw new CodegenTaskException("未存在类型为：" + type.name() + "sdk代码的目录");
        }
        return sdk;
    }

    /**
     * 返回上传的临时sdk文件地址
     * /tempdir/projectName/branch/type.zip
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     * @param moduleName  模块名称
     * @param type        client type
     * @return sdk zip文件地址
     */
    private String getTempSdkFilepath(String projectName, String branch, String moduleName, ClientProviderType type) {
        String directoryPath = String.join(File.separator, this.uploadTempDir, projectName, branch, moduleName);
        createDirectoryRecursively(directoryPath);
        String filepath = String.join(File.separator, directoryPath, type.name().toLowerCase());
        return String.join(".", filepath, "zip");
    }

    private String getModuleNameOrDefault(String moduleName) {
        if (moduleName == null) {
            return DEFAULT_MODULE_NAME;
        }
        return moduleName;
    }

    private String getBranchOrDefault(String branch) {
        if (branch == null) {
            return sourcecodeRepository.getMasterBranchName();
        }
        return branch;
    }

    private static void nioTransferCopy(MultipartFile source, File target) throws IOException {

        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = (FileInputStream) source.getInputStream();
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } finally {
            IOUtils.close(inStream);
            IOUtils.close(in);
            IOUtils.close(outStream);
            IOUtils.close(out);
        }
    }


    /**
     * 递归创建目录
     *
     * @param directoryPath 目录路径
     */
    public static void createDirectoryRecursively(String directoryPath) {
        String[] filepathParts = directoryPath.split(String.format("\\%s", File.separator));
        StringBuilder path = new StringBuilder();
        for (String part : filepathParts) {
            path.append(File.separator).append(part);
            File directory = new File(path.toString());
            if (!directory.exists()) {
                boolean r = directory.mkdir();
                log.debug("创建目录：{}，结果：{}", directory.getPath(), r ? "成功" : "失败");
            }
        }
    }
}
