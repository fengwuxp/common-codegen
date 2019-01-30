package com.wuxp.codegen.dragon.core.utils;

/**
 * 大小写转换
 */
public final class ToggleCaseUtil {

    private ToggleCaseUtil() {
    }

    /**
     * 将字符串的第一个字符大小写取反
     *
     * @param str
     * @return
     */
    public static String toggleFirstChart(String str) {
        if (str == null) {
            return null;
        }
        char[] chars = str.toCharArray();
        chars[0] = (char) (chars[0] ^ 0x20);
        return new String(chars);
    }



}
