package com.wuxp.codegen.client.maven;

import com.wuxp.codegen.core.ClientProviderType;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import okhttp3.*;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 用于下载sdk的下载者
 *
 * @author wuxp
 */
@Slf4j
public class CodegenDownloader {
    private static final String CODEGEN_UPLOAD_URI = "/codegen/loong/sdk_code";

    /**
     * codegen-server的服务端地址
     */
    private final String loongCodegenServer;

    /**
     * 项目名称
     */
    private final String projectName;

    /**
     * 需要下载的分支
     */
    private final String branch;

    public CodegenDownloader(String loongCodegenServer, String projectName, String branch) {
        this.loongCodegenServer = loongCodegenServer;
        this.projectName = projectName;
        this.branch = branch;
    }

    public void download(String moduleName, ClientProviderType type, String folder) {

        if (log.isInfoEnabled()) {
            log.info("下载sdk：{} 到目录：{}", type.name(), folder);
        }

        //projectName=common-codegen&type=SPRING_CLOUD_OPENFEIGN
        Map<String, String> queryParams = new HashMap<>(8);
        queryParams.put("projectName", projectName);
        queryParams.put("branch", branch);
        queryParams.put("moduleName", moduleName);
        queryParams.put("type", type.name());
        StringBuilder url = new StringBuilder(String.format("%s%s", loongCodegenServer, CODEGEN_UPLOAD_URI));
        url.append("?");
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if (val == null) {
                continue;
            }
            url.append(key).append("=").append(val).append("&");
        }
        url.deleteCharAt(url.length() - 1);
        File codegenSdk = download(url.toString(), folder, String.format("%s.zip", type.name().toLowerCase()));
        if (codegenSdk == null) {
            log.error("下载sdk失败");
            return;
        }
        ZipFile zFile = new ZipFile(codegenSdk);
        zFile.setCharset(StandardCharsets.UTF_8);
        try {
            zFile.extractAll(folder);
        } catch (ZipException exception) {
            log.error("解压zip文件到：{}", folder);
        }
        codegenSdk.deleteOnExit();
        // 将文件复制到src/main/java目录下
        File srcDir = new File(String.join(File.separator, folder, type.name().toLowerCase()), "src");
        try {
            FileSystemUtils.copyRecursively(srcDir, new File(String.join(File.separator, "src", "main", "java")));
        } catch (IOException exception) {
            log.error("复制文件失败", exception);
        }
        File tempdir = new File(String.join(File.separator, folder, type.name().toLowerCase()));
        FileSystemUtils.deleteRecursively(tempdir);

    }

    /**
     * @param url          下载连接
     * @param destFileDir  下载的文件储存目录
     * @param destFileName 文件名称
     * @return 下载后的文件对象
     */
    private File download(final String url, final String destFileDir, final String destFileName) {

        if (log.isInfoEnabled()) {
            log.info("从：{}下载sdk到目录：{}", url, destFileDir);
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        OkHttpClient client = initOkHttp();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        //储存下载文件的目录
        File dir = new File(destFileDir);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
            if (log.isDebugEnabled()) {
                log.debug("目录创建{}{}", dir.getAbsolutePath(), mkdirs ? "成功" : "失败");
            }
        }
        File file = new File(dir, destFileName);
        file.deleteOnExit();
        try {
            boolean newFile = file.createNewFile();
            if (log.isDebugEnabled()) {
                log.debug("文件创建{}{}", dir.getAbsolutePath(), newFile ? "成功" : "失败");
            }
        } catch (IOException exception) {
            log.error("创建文件失败，filepath={}", file.getAbsolutePath(), exception);
            return null;
        }
        //异步请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException exception) {
                countDownLatch.countDown();
                log.error("下载失败", exception);
            }

            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody body = response.body();
                if (body == null) {
                    log.error("获取响应失败");
                    return;
                }
                InputStream is = null;
                FileOutputStream fos = null;

                try {
                    is = body.byteStream();
                    long total = body.contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    int len = 0;
                    byte[] buf = new byte[2048];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        //下载中更新进度条
                        log.info("下载文件进度:{}", progress);
                    }
                    fos.flush();
                    //下载完成
                } catch (Exception exception) {
                    log.error("下载文件失败，message:{}", exception.getMessage(), exception);
                } finally {
                    countDownLatch.countDown();
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException exception) {
                        log.error("close io异常", exception);
                    }
                }
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException exception) {
            log.error("下载被中断", exception);
            Thread.currentThread().interrupt();
            return null;
        }
        log.error("下载已完成");
        return file;
    }

    private OkHttpClient initOkHttp() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
    }

}
