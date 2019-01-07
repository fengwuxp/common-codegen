package com.wuxp.codegen.scanner.util;

import java.util.ArrayList;
import java.util.List;

public class FileStringUtils {

    private static List<String> systemJars = new ArrayList<String>() {
        {
            add("charsets.jar");
            add("deploy.jar");
            add("javaws.jar");
            add("jce.jar");
            add("jfr.jar");
            add("jfxswt.jar");
            add("jsse.jar");
            add("management-agent.jar");
            add("plugin.jar");
            add("resources.jar");
            add("rt.jar");
            add("access-bridge-64.jar");
            add("cldrdata.jar");
            add("dnsns.jar");
            add("jaccess.jar");
            add("jfxrt.jar");
            add("localedata.jar");
            add("nashorn.jar");
            add("sunec.jar");
            add("sunjce_provider.jar");
            add("sunmscapi.jar");
            add("sunpkcs11.jar");
            add("zipfs.jar");
            add("idea_rt.jar");
        }
    };

    public static String getSimpleFileName(String filePath) {
        if (filePath.contains("\\")) {
            return filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
        } else {
            return filePath;
        }
    }

    public static String getFileExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return fileName;
        }
    }

    public static String getFileName(String fileFullName) {
        if (fileFullName.contains("/") && fileFullName.contains(".")) {
            return fileFullName.substring(fileFullName.lastIndexOf("/") + 1, fileFullName.lastIndexOf("."));
        } else {
            return fileFullName;
        }
    }

    public static boolean isJar(String fileName) {
        return getFileExtension(fileName).equals("jar");
    }

    public static boolean isClass(String fileName) {
        return getFileExtension(fileName).equals("class");
    }

    public static String normalizeClassName(String className) {
        String newClassName = className.replace("/", ".").replace("\\", ".");
        if (newClassName.contains(".class")) {
            return newClassName.substring(0, newClassName.lastIndexOf(".class"));
        } else {
            return newClassName;
        }
    }

    public static String[] getClassPathElements() {
        return System.getProperty("java.class.path").split(";");
    }

    public static boolean isSystemJar(String file) {
        return systemJars.contains(getSimpleFileName(file));
    }

    public static String substringPath(String fileName, String path) {

        String replace = fileName.replace(path, "");
        if (OsUtil.isWin()) {
            return replace;
        }
        return replace.substring(1);
    }
}
