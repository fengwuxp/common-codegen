package com.wuxp.codegen.loong;

import com.wuxp.codegen.core.ClientProviderType;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 用于上传sdk的生成结果
 * <p>
 * 按照{@link ClientProviderType}逐一压缩文件，并上传到loong-quick-server服务端
 * </p>
 *
 * @author wuxp
 */
@Slf4j
public class CodegenSdkUploader {

    public static final String QUERY_SERVER_ADDRESS = "loong-quick-server";

    public static final String PROJECT_NAME = "loong.codegen.project.name";

    public static final String PROJECT_BRANCH_NAME = "loong.codegen.project.branch";

    public static final String PROJECT_MODULE_NAME = "loong.codegen.project.module";

    private static final String CODEGEN_UPLOAD_URI = "/codegen/loong/sdk_code";

    /**
     * 用于上传sdk的uri
     */
    private static final String QUICK_SERVER_ADDRESS = getSystemEnv(QUERY_SERVER_ADDRESS);

    private final String outputDir;

    private final String projectName;

    @Setter
    private String quickServerAddress;

    private OkHttpClient client;

    public CodegenSdkUploader(String outputDir) {
        this(outputDir, null);
    }

    public CodegenSdkUploader(String outputDir, String projectName) {
        this.outputDir = outputDir;
        this.projectName = getProjectName(projectName);
        this.quickServerAddress = QUICK_SERVER_ADDRESS;
        this.initOkHttp();
    }

    public void upload() {
        this.upload(null, null);
    }

    public void upload(String branch) {
        this.upload(branch, null);
    }

    public void upload(String branch, String moduleName) {
        if (projectName == null) {
            log.error("projectName must not null");
            return;
        }
        if (quickServerAddress == null) {
            log.error("未获取到上传文件的服务端地址，请先设置quickServerAddress");
            return;
        }
        branch = getProjectBranch(branch);
        moduleName = getProjectModule(moduleName);
        Map<String, String> params = new HashMap<>(8);
        params.put("projectName", projectName);
        params.put("branch", branch);
        params.put("moduleName", moduleName);
        Arrays.asList(ClientProviderType.values()).forEach(type -> {
            params.put("type", type.name());
            this.uploadSdk(String.format("%s%s%s", outputDir, File.separator, type.name().toLowerCase()), params);
        });
    }


    private void uploadSdk(String sdkDir, Map<String, String> params) {
        if (!new File(sdkDir).exists()) {
            return;
        }
        if (log.isInfoEnabled()) {
            log.info("准备上传sdk{}到{}", sdkDir, getCodegenUploadUrl());
        }
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(String.format("%s.zip", sdkDir));
            zipFile.addFolder(new File(sdkDir));
        } catch (IOException exception) {
            log.error("压缩sdk文件失败：{}，message：{}", sdkDir, exception.getMessage(), exception);
            return;
        }
        Request fileRequest = getFileRequest(zipFile.getFile(), params);
        try {
            Response response = client.newCall(fileRequest).execute();
            boolean successful = response.isSuccessful();
            if (log.isDebugEnabled()) {
                log.info("上传sdk:{}，本地文件路径：{}，server message：{}", successful ? "成功" : "失败", sdkDir,response.message());
            }
            response.close();
        } catch (IOException exception) {
            log.error("上传sdk文件失败：{}，message：{}", sdkDir, exception.getMessage(), exception);
        } finally {
            zipFile.getFile().deleteOnExit();
        }
    }

    private void initOkHttp() {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    private Request getFileRequest(File file, Map<String, String> maps) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        maps.forEach((key, val) -> {
            if (val != null) {
                builder.addFormDataPart(key, val);
            }
        });
        builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"" + file.getName() + "\""), RequestBody.create(MediaType.parse("application/octet-stream"), file));
        RequestBody body = builder.build();
        return new Request.Builder().url(getCodegenUploadUrl()).post(body).build();
    }

    private String getProjectName(String projectName) {
        if (projectName != null) {
            return projectName;
        }
        String name = getSystemEnv(PROJECT_NAME);
        if (name != null) {
            return name;
        }
        String file = getSystemEnv("project.basedir");
        if (file == null) {
            return null;
        }
        String[] values = file.split(File.separator);
        return values[values.length - 1];
    }

    private String getProjectBranch(String branch) {
        if (branch != null) {
            return branch;
        }
        String branchName = getSystemEnv(PROJECT_BRANCH_NAME);
        if (branchName != null) {
            return branchName;
        }
        return "master";
    }

    private String getProjectModule(String moduleName) {
        if (moduleName != null) {
            return moduleName;
        }
        return getSystemEnv(PROJECT_MODULE_NAME);
    }

    private String getCodegenUploadUrl() {
        return String.format("%s%s", quickServerAddress, CODEGEN_UPLOAD_URI);
    }


    private static String getSystemEnv(String... names) {
        return Arrays.stream(names).map(name -> {
            String env = System.getenv(name);
            if (env == null) {
                env = System.getProperty(name);
            }
            return env;
        }).filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
}
