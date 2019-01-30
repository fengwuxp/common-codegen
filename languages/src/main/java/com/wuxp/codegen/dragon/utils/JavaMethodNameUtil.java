package com.wuxp.codegen.dragon.utils;

import com.wuxp.codegen.core.utils.ToggleCaseUtil;

import java.util.regex.Pattern;

/**
 * 处理java method name
 */
public final class JavaMethodNameUtil {

    private static final String IS_GET_METHOD_NAME = "^get[A-Z]+\\w*";

    private static final String IS_IS_METHOD_NAME = "^is[A-Z]+\\w*";

    /**
     * 是否为get方法或is 方法
     *
     * @param methodName
     * @return
     */
    public static boolean isGetMethodOrIsMethod(String methodName) {
        if (methodName == null) {
            return false;
        }
        return Pattern.matches(IS_GET_METHOD_NAME, methodName) || Pattern.matches(IS_IS_METHOD_NAME, methodName);
    }

    /**
     * 去除方法名称的 get 或者 is 前缀
     * @param methodName
     * @return
     */
    public static String replaceGetOrIsPrefix(String methodName) {
        if (methodName == null) {
            return null;
        }
        //转换方法名称
        String methodMetaName = methodName;
        if (Pattern.matches(IS_GET_METHOD_NAME, methodMetaName)) {
            methodMetaName = methodMetaName.substring(3);
        } else if (Pattern.matches(IS_IS_METHOD_NAME, methodMetaName)) {
            methodMetaName = methodMetaName.substring(2);
        } else {
            return methodMetaName;
        }

        return ToggleCaseUtil.toggleFirstChart(methodMetaName);
    }
}
