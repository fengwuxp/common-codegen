package com.wuxp.codegen.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://github.com/anthonynsimon/jurl"></a>
 * <p>
 * PathResolver is a utility class that resolves a reference path against a base path.
 *
 * @author https://github.com/anthonynsimon/jurl
 */
public final class PathResolver {

    private static final String separator = File.separator;

    /**
     * Disallow instantiation of class.
     */
    private PathResolver() {
    }

    /**
     * Returns a resolved path.
     * <p>
     * For example:
     * <p>
     * resolve("/some/path", "..") == "/some"
     * resolve("/some/path", ".") == "/some/"
     * resolve("/some/path", "./here") == "/some/here"
     * resolve("/some/path", "../here") == "/here"
     */
    public static String resolve(String base, String ref) {
        if (base == null) {
            return null;
        }
        if (ref == null) {
            return base;
        }
        String merged = merge(base, ref);
        if (merged.isEmpty()) {
            return "";
        }
        String[] parts = merged.split(String.format("\\%s", separator), -1);
        return resolve(parts);
    }

    /**
     * Returns the two path strings merged into one.
     * <p>
     * For example:
     * <qp>
     * merge("/some/path", "./../hello") == "/some/./../hello"
     * merge("/some/path/", "./../hello") == "/some/path/./../hello"
     * merge("/some/path/", "") == "/some/path/"
     * merge("", "/some/other/path") == "/some/other/path"
     */
    private static String merge(String base, String ref) {
        String merged;
        if (ref == null || ref.isEmpty()) {
            merged = base;
        } else if (ref.charAt(0) != separator.charAt(0) && base != null && !base.isEmpty()) {
            int i = base.lastIndexOf(separator);
            merged = base.substring(0, i + 1) + ref;
        } else {
            merged = ref;
        }

        if (merged == null || merged.isEmpty()) {
            return "";
        }

        return merged;
    }

    /**
     * Returns the resolved path parts.
     * <p>
     * Example:
     * <p>
     * resolve(String[]{"some", "path", "..", "hello"}) == "/some/hello"
     */
    private static String resolve(String[] parts) {
        if (parts.length == 0) {
            return "";
        }

        List<String> result = new ArrayList<>();

        for (String part : parts) {
            switch (part) {
                case "":
                case ".":
                    // Ignore
                    break;
                case "..":
                    if (!result.isEmpty()) {
                        result.remove(result.size() - 1);
                    }
                    break;
                default:
                    result.add(part);
                    break;
            }
        }

        // Get last element, if it was '.' or '..' we need
        // to end in a slash.
        switch (parts[parts.length - 1]) {
            case ".":
            case "..":
                // Add an empty last string, it will be turned into
                // a slash when joined together.
                result.add("");
                break;
            default:

        }
        return separator + String.join(separator, result);
    }
}
