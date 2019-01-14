package com.oaknt.codegen.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

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

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {

        log.debug("删除文件{}", sPath);
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }

        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }

        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
