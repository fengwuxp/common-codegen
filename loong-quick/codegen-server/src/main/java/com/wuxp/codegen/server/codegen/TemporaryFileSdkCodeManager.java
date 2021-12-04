package com.wuxp.codegen.server.codegen;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.util.CodegenFileUtils;
import com.wuxp.codegen.server.plugins.CodegenPluginExecuteStrategy;
import com.wuxp.codegen.server.task.CodegenTaskException;
import com.wuxp.codegen.server.vcs.SourcecodeRepository;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * 基于基于 temp file 管理 sdk 代码
 *
 * @author wuxp
 */
@Slf4j
public class TemporaryFileSdkCodeManager implements SdkCodeManager {

    private static final String OUTPUT_DIR = String.join(File.separator, "target", "codegen-sdk", "loong");

    private final SourcecodeRepository sourcecodeRepository;

    private final CodegenPluginExecuteStrategy codegenPluginExecuteStrategy;

    private final String uploadTempDir;

    public TemporaryFileSdkCodeManager(SourcecodeRepository sourcecodeRepository, CodegenPluginExecuteStrategy codegenPluginExecuteStrategy, String uploadTempDir) {
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
            CodegenFileUtils.createDirectoryRecursively(uploadTempDir);
        }
        this.uploadTempDir = uploadTempDir;
    }

    @Override
    public void storage(SdkCodeDescriptor descriptor, InputStream inputStream) {
        String temporaryDir = this.createTemporaryDir(descriptor);
        if (log.isInfoEnabled()) {
            log.info("上传项目：{}，分支：{}，的{}sdk到：{}", descriptor.getProjectName(), descriptor.getBranch(), descriptor.getType(), temporaryDir);
        }
        File file = new File(temporaryDir);
        file.deleteOnExit();
        try {
            Assert.isTrue(file.createNewFile(), "创建 sdk 临时文件失败，temp path = " + temporaryDir);
            writeFile(inputStream, file);
        } catch (IOException exception) {
            log.error("上传sdk文件异常：{}", exception.getMessage(), exception);
        }
    }

    @Override
    public File get(SdkCodeDescriptor descriptor) {
        String tempSdkFilepath = this.createTemporaryDir(descriptor);
        File tempSdkFile = new File(tempSdkFilepath);
        // 存在已上传的sdk文件
        if (tempSdkFile.exists()) {
            return tempSdkFile;
        }
        // 不存在，尝试从本地仓库中获取
        File sdkFile = getSdkCodeByLocalRepository(descriptor);
        return zipSdkFile(descriptor.getType(), sdkFile);
    }

    private File getSdkCodeByLocalRepository(SdkCodeDescriptor descriptor) {
        try {
            return findSdkCodeByLocalRepository(descriptor);
        } catch (Exception exception) {
            log.error("从 sourcecodeRepository获取 sdk失败{}，message：{}", exception.getMessage(), exception);
            throw new CodegenTaskException(exception);
        }
    }

    private File findSdkCodeByLocalRepository(SdkCodeDescriptor descriptor) {
        String localDirectory = sourcecodeRepository.getLocalDirectory(descriptor.getProjectName(), descriptor.getBranch());
        File baseDir = getSdkCodeSwaggerBaseDir(localDirectory, descriptor.getModuleName());
        File sdkCodeFile = new File(String.join(File.separator, baseDir.getAbsolutePath(), descriptor.getType().name().toLowerCase()));
        Assert.isTrue(sdkCodeFile.exists(), "client provider type：" + descriptor.getType().name() + " sdk 代码的目录不存在");
        return sdkCodeFile;
    }

    private File getSdkCodeSwaggerBaseDir(String localDirectory, String moduleName) {
        String moduleFilepath = codegenPluginExecuteStrategy.findModuleFilepath(localDirectory, moduleName);
        String sdkBasePath = String.join(File.separator, moduleFilepath, OUTPUT_DIR);
        return Stream.of("swagger_2", "swagger_3")
                .map(swagger -> new File(String.join(File.separator, sdkBasePath, swagger)))
                .filter(File::exists)
                .findFirst()
                .orElseThrow(() -> new CodegenTaskException(String.format("sdk 代码生成的目录：%s 不存在 swagger 目录", sdkBasePath)));
    }

    private File zipSdkFile(ClientProviderType type, File sdkFile) {
        try {
            ZipFile zipFile = new ZipFile(String.format("%s-%s.zip", sdkFile.getName(), type.name().toLowerCase()));
            zipFile.addFolder(sdkFile);
            return zipFile.getFile();
        } catch (ZipException exception) {
            log.error("zip 压缩 sdk 文件失败，sdk文件路径：{}，message：{}", sdkFile.getAbsolutePath(), exception.getMessage(), exception);
            throw new CodegenTaskException(exception);
        }
    }

    /**
     * 返回上传的临时sdk文件地址
     * /tempDir/projectName/branch/type.zip
     *
     * @param descriptor sdk code 描述
     * @return sdk zip文件地址
     */
    private String createTemporaryDir(SdkCodeDescriptor descriptor) {
        String tempDir = String.join(File.separator, this.uploadTempDir, descriptor.getProjectName(), descriptor.getBranch(), descriptor.getModuleName());
        CodegenFileUtils.createDirectoryRecursively(tempDir);
        String filepath = String.join(File.separator, tempDir, descriptor.getType().name().toLowerCase());
        return Paths.get(String.join(".", filepath, "zip")).normalize().toString();
    }

    private static void writeFile(InputStream source, File target) throws IOException {
        try (FileOutputStream outStream = new FileOutputStream(target);) {
            StreamUtils.copy(source, outStream);
        } catch (Exception exception) {
            log.error("write file failure, target path = " + target.getAbsolutePath(), exception);
        }
    }
}
