package com.wuxp.codegen.core.util;

/**
 * 类加载工具
 * 用于统一替换类加载的实现
 *
 * @author wuxp
 */
public final class ClassLoaderUtils {

    private ClassLoaderUtils() {
        throw new AssertionError();
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(String name) throws ClassNotFoundException {
        return (Class<T>) Thread.currentThread().getContextClassLoader().loadClass(name);
    }


}
