package com.wuxp.codegen.core.util;

/**
 * 类加载工具
 *
 * @author wuxp
 */
public class ClassLoaderUtils {


    public static <T> Class<T> loadClass(String name) throws ClassNotFoundException {
        return (Class<T>) Class.forName(name);
    }

}
