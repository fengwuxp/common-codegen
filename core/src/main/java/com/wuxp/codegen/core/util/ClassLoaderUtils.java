package com.wuxp.codegen.core.util;

/**
 * 类加载工具
 * 用于同一替换类加载的实现
 *
 * @author wuxp
 */
public final class ClassLoaderUtils {

    private ClassLoaderUtils() {
    }

    public static <T> Class<T> loadClass(String name) throws ClassNotFoundException {
        return (Class<T>) Class.forName(name);
    }

}
