package com.wuxp.codegen.scanner.util;

import java.util.Properties;


/**
 * 判断操作系统
 */
public class OsUtil {

    private static final Properties SYSTEM_PROP = System.getProperties();

    public static boolean isWin() {
        String os = SYSTEM_PROP.getProperty("os.name");
        if (os == null) {
            return false;
        }
        if (os.toLowerCase().contains("win")) {
            return true;
        }

        return false;

    }

    public static boolean isLinux() {

        String os = SYSTEM_PROP.getProperty("os.name");
        if (os == null) {
            return false;
        }

        if (os.toLowerCase().contains("linux")) {
            return true;
        }

        return false;
    }
}
