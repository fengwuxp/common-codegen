package com.wuxp.codegen.core.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * PathResolveUtils is a utility class that resolves a reference path against a base path.
 *
 * @author wuxp
 */
@Slf4j
public final class PathResolveUtils {


    /**
     * Disallow instantiation of class.
     */
    private PathResolveUtils() {
    }

    /**
     * 获取文件的相对路径
     *
     * @param targetFilePath  目标路径
     * @param currentFilePath 当前路径
     * @return 计算得到的路径
     */
    @Deprecated
    public static String getRelativePath(String targetFilePath, String currentFilePath) {

        Object[] a1 = Arrays.stream(targetFilePath.split("/"))
                .filter(t -> !".".equals(t))
                .filter(StringUtils::hasText)
                .toArray();

        Object[] a2 = Arrays.stream(currentFilePath.split("/"))
                .filter(t -> !".".equals(t))
                .filter(StringUtils::hasText)
                .toArray();

        StringBuilder sb = new StringBuilder();

        int idx = 0;
        while (idx < a2.length
                && idx < a1.length
                && a1[idx].equals(a2[idx])) {
            idx++;
        }

        int temp = idx;
        while (temp++ < a2.length) {
            sb.append("../");
        }

        while (idx < a1.length) {
            sb.append(a1[idx++]).append("/");
        }

        if (log.isDebugEnabled()) {
            log.debug("[" + targetFilePath + "] --> [" + currentFilePath + "] --> [" + sb + "]");
        }
        return sb.toString();
    }

    public static String relative(String path, String ref) {
        return relative(path, ref, File.separator);
    }


    /**
     * 计算2个路径的相对路径
     * <p>
     * relative("/api/b/c","/api/b/c","/")  ==>"."
     * relative("/api/b/c","/api/b/d","/")  ==>"../c"
     * relative("/api/b/c/d","/api/b/d","/")  ==>"../c/d"
     * relative("/api/b/c/d","/api/b/d/e","/")  ==>"../../c/d"
     * relative("/api/b/c/","/api/b/d/e","/")  ==>"../../c"
     * relative("/api/b/","/api/b/d/e","/")  ==>"../.."
     * relative("/api/","/api/b/d/e","/")  ==>"../../.."
     * relative("/api/b/c/d","/api/b","/")  ==>"./c/d"
     * relative("/api/b/c/d","/cpi/b","/")  ==>"../../api/b/c/d"
     * </p>
     *
     * @param path      基础路径
     * @param ref       目标路径
     * @param separator 路径分隔符
     * @return 2个路径的相对路径
     */
    public static String relative(String path, String ref, String separator) {
        boolean unixSeparator = "/".equals(separator);
        path = FilenameUtils.normalizeNoEndSeparator(path, unixSeparator);
        if (!ref.startsWith(".")) {
            ref = FilenameUtils.normalizeNoEndSeparator(ref, unixSeparator);
        }
        if (ref == null) {
            return path;
        }
        boolean isOther = ref.startsWith(".") || ref.startsWith(separator);
        if (!isOther) {
            return FilenameUtils.normalizeNoEndSeparator(path + separator + ref, unixSeparator);
        }
        String[] paths = splitPaths(path, separator);
        String[] refs = splitPaths(ref, separator);
        Deque<String> results = new ArrayDeque<>();
        int noEqIndex = 0;
        for (int i = 0; i < refs.length; i++) {
            if (paths.length <= i || !paths[i].equals(refs[i])) {
                break;
            }
            noEqIndex++;
        }

        // push paths in stack
        for (int k = paths.length - 1; k >= noEqIndex; k--) {
            pushLeftPaths(results, paths[k]);
        }

        // push refs in stack
        if (noEqIndex > 0) {
            for (int k = noEqIndex; k < refs.length; k++) {
                String part = refs[k];
                pushRefsOnEq(results, part);
            }
        } else {
            for (int k = noEqIndex; k < refs.length; k++) {
                String part = refs[k];
                pushRefsNotEq(results, part);
            }
        }
        if (results.isEmpty()) {
            return ".";
        }
        return String.join(separator, results);
    }

    /**
     * 计算路径的值
     *
     * @param paths 保存路径内容的Stack
     * @param part  大于相同路径的每个路径组成部分
     */
    private static void pushLeftPaths(Deque<String> paths, String part) {
        switch (part) {

            case ".":
                break;
            case "..":
                paths.pop();
                break;
            // 防止第一个斜杆丢失
            case "":
            default:
                paths.push(part);
        }
    }

    /**
     * 目标路径和当前路径存在相同的路径
     *
     * @param paths 保存路径内容的Stack
     * @param part 大于相同路径的每个路径组成部分
     */
    private static void pushRefsOnEq(Deque<String> paths, String part) {
        switch (part) {
            case "":
            case ".":
                break;
            case "..":
                paths.pop();
                break;
            default:
                paths.push("..");
        }
    }

    /**
     * 目标路径和当前路径不存在相同的路径
     *
     * @param paths 保存路径内容的Stack
     * @param part 大于相同路径的每个路径组成部分
     */
    private static void pushRefsNotEq(Deque<String> paths, String part) {
        switch (part) {
            case "":
            case ".":
                break;
            case "..":
                paths.removeLast();
                break;
            default:
                paths.addLast(part);
        }
    }

    private static String[] splitPaths(String val, String separator) {
        return val.split(String.format("\\%s", separator), -1);
    }
}
