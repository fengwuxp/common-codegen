package com.wuxp.codegen.meta.util;

import com.wuxp.codegen.loong.path.PathResolve;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

/**
 * 文件操作相关工具
 *
 * @author wuxp
 */
@Slf4j
public final class FileUtils {

    private FileUtils() {
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
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
            }
            if (!flag) {
                break;
            }
        }
        if (!flag) {
            return false;
        }
        //v删除当前目录
        return dirFile.delete();
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
            flag = file.delete();
        }
        return flag;
    }

    public static String packageNameToFilePath(String packagePath) {
        // 此处必须使用replaceAll
        return packagePath.replaceAll("\\.", PathResolve.RIGHT_SLASH);
    }
}
