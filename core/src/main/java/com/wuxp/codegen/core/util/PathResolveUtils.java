package com.wuxp.codegen.core.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

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
        if (ref == null) {
            return path;
        }

        boolean unixSeparator = "/".equals(separator);
        path = FilenameUtils.normalizeNoEndSeparator(path, unixSeparator);
        if (!ref.startsWith(".")) {
            ref = FilenameUtils.normalizeNoEndSeparator(ref, unixSeparator);
        }

        boolean isOther = ref.startsWith(".") || ref.startsWith(separator);
        if (!isOther) {
            return FilenameUtils.normalizeNoEndSeparator(path + separator + ref, unixSeparator);
        }

        // 计算相对路径
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
     * @param paths 保存路径内容的Stack
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
