package com.wuxp.codegen.core.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayDeque;
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
        throw new AssertionError();
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
     * @param ref       计算路径
     * @param separator 路径分隔符
     * @return 2个路径的相对路径
     */
    public static String relative(String path, String ref, String separator) {
        if (!StringUtils.hasText(ref)) {
            return path;
        }

        boolean unixSeparator = "/".equals(separator);
        if (!ref.startsWith(".")) {
            ref = FilenameUtils.normalizeNoEndSeparator(ref, unixSeparator);
        }

        path = FilenameUtils.normalizeNoEndSeparator(path, unixSeparator);
        if (isSubdirectory(ref, separator)) {
            return FilenameUtils.normalizeNoEndSeparator(path + separator + ref, unixSeparator);
        }

        // 计算相对路径
        String[] paths = splitPaths(path, separator);
        String[] refs = splitPaths(ref, separator);
        Deque<String> results = new ArrayDeque<>();
        int equalsPartIndex = computeEqualsPartIndex(paths, refs);

        // 将相等的 part push   stack
        for (int k = paths.length - 1; k >= equalsPartIndex; k--) {
            pushLeftPaths(results, paths[k]);
        }

        boolean hasEqualsPart = equalsPartIndex > 0;
        // push refs in stack
        for (int k = equalsPartIndex; k < refs.length; k++) {
            String part = refs[k];
            if (hasEqualsPart) {
                pushRefsOnEq(results, part);
            } else {
                pushRefsNotEq(results, part);
            }
        }

        if (results.isEmpty()) {
            return ".";
        }
        return String.join(separator, results);
    }

    /**
     * 计算 2 个路径相等的 part index
     *
     * @param paths 基础路径
     * @param refs  相对路径
     * @return 路径相等的 part index
     */
    private static int computeEqualsPartIndex(String[] paths, String[] refs) {
        int result = 0;
        for (int i = 0; i < refs.length; i++) {
            if (paths.length <= i || !paths[i].equals(refs[i])) {
                break;
            }
            result++;
        }
        return result;
    }

    private static boolean isSubdirectory(String ref, String separator) {
        // 不是以 . 或 / 开头，说明是子目录
        return !(ref.startsWith(".") || ref.startsWith(separator));
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
     * @param paths 保存路径内容的 Stack
     * @param part  大于相同路径的每个路径组成部分
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
     * @param paths 保存路径内容的 Stack
     * @param part  大于相同路径的每个路径组成部分
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
