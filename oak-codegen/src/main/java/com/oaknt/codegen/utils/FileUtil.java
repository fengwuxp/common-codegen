package com.oaknt.codegen.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * 文件操作相关工具
 */
@Slf4j
public final class FileUtil {

    public boolean fileExists(File file) {
        return file.exists();
    }

    /**
     * 递归创建目录
     *
     * @param directoryName
     * @return 文件夹目录
     */
    public static void createDirectory(String directoryName) {
        String[] filePaths = directoryName.split("\\\\");
        StringBuilder filePath = new StringBuilder("");

        for (int i = 0; i < filePaths.length; i++) {
            filePath.append("\\").append(filePaths[i]);
            File directory = new File(filePath.toString());
            if (!directory.exists()) {
                directory.mkdir();
                log.debug("创建目录{}", directory.getPath());
            }
        }
    }
}
