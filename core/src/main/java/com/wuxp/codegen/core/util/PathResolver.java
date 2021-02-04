package com.wuxp.codegen.core.util;

import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * https://github.com/anthonynsimon/jurl/blob/master/src/main/java/com/anthonynsimon/url/PathResolver.java
 * PathResolver is a utility class that resolves a reference path against a base path.
 *
 * @author wuxp
 */
public final class PathResolver {

    private static final String FILE_SEPARATOR = File.separator;

    /**
     * Disallow instantiation of class.
     */
    private PathResolver() {
    }


    /**
     * 计算2个路径的相对路径
     * <p>
     * calculatingRelativePath("/api/b/c","/api/b/c","/")  ==>"."
     * calculatingRelativePath("/api/b/c","/api/b/d","/")  ==>"../c"
     * calculatingRelativePath("/api/b/c/d","/api/b/d","/")  ==>"../c/d"
     * calculatingRelativePath("/api/b/c/d","/api/b/d/e","/")  ==>"../../c/d"
     * calculatingRelativePath("/api/b/c/","/api/b/d/e","/")  ==>"../../c"
     * calculatingRelativePath("/api/b/","/api/b/d/e","/")  ==>"../.."
     * calculatingRelativePath("/api/","/api/b/d/e","/")  ==>"../../.."
     * calculatingRelativePath("/api/b/c/d","/api/b","/")  ==>"./c/d"
     * calculatingRelativePath("/api/b/c/d","/cpi/b","/")  ==>"../../api/b/c/d"
     * </p>
     *
     * @param target    被用于计算的路径
     * @param path      期望计算的路径
     * @param separator 路径分隔符
     * @return 2个路径的相对路径
     */
    public static String calculatingRelativePath(String target, String path, String separator) {
        if (!StringUtils.hasText(target) || !StringUtils.hasText(path)) {
            return null;
        }
        if (path.equals(target)) {
            return ".";
        }

        String[] targetPaths = target.split(separator);
        String[] paths = path.split(separator);
        List<String> results = new ArrayList<>(Arrays.asList(targetPaths));
        int eqCount = 0;
        for (int i = 0; i < paths.length; i++) {
            if (targetPaths.length <= i) {
                break;
            }
            if (paths[i].equals(targetPaths[i])) {
                eqCount++;
                results.set(i, null);
            } else {
                break;
            }
        }
        // 2个路径的相差值
        int difference = paths.length - eqCount;
        while (difference-- > 0) {
            results.add(0, "..");
        }
        if (eqCount >= paths.length) {
            // 相同的路径大于了期望被计算的路径，说明从被计算的当前路径开始
            results.set(0, ".");
        }
        return results.stream().filter(Objects::nonNull).collect(Collectors.joining(separator));

    }


    /**
     * 计算2个路径的相对路径
     *
     * @param path
     * @param ref
     * @param separator
     * @return
     */
    public static String relative(String path, String ref, String separator) {

        String[] paths = splitPaths(path, separator);
        String[] refs = splitPaths(ref, separator);

        Stack<String> results = new Stack<>();

        int noEqIndex = 0;
        for (int i = 0; i < refs.length; i++) {
            if (paths.length <= i) {
                break;
            }
            if (paths[i].equals(refs[i])) {
                // ignore
                noEqIndex++;
            } else {
                break;
            }

        }
        // push paths in stack
        for (int k = paths.length - 1; k >= noEqIndex; k--) {
            pushLeftPaths(results, paths[k]);
        }

        // push refs in stack
        for (int k = noEqIndex; k < refs.length; k++) {
            pushRefs(results, refs[k]);
        }

        if (results.isEmpty()) {
            return ".";
        }

        List<String> values = new ArrayList<>(results);
        Collections.reverse(values);
        return String.join(separator, values);
    }

    private static void pushLeftPaths(Stack<String> paths, String part) {
        switch (part) {
            case "":
            case ".":
                break;
            case "..":
                paths.pop();
                break;
            default:
                paths.push(part);

        }
    }

    private static void pushRefs(Stack<String> paths, String part) {
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

    private static void popPaths(Stack<String> paths, List<String> values) {
        for (String part : paths) {
            switch (part) {
                case "":
                case ".":
                    break;
                case "..":
                    values.add(part);
                    break;
                default:
                    paths.push("..");

            }
        }
    }


    private static String[] splitPaths(String val, String separator) {
        return val.split(String.format("\\%s", separator), -1);
    }
}
