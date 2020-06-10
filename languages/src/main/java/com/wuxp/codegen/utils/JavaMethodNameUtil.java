package com.wuxp.codegen.utils;

import com.wuxp.codegen.core.utils.ToggleCaseUtil;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理java method name
 */
public final class JavaMethodNameUtil {

    private static final String IS_GET_METHOD_NAME = "^get[A-Z]+\\w*";

    private static final String IS_IS_METHOD_NAME = "^is[A-Z]+\\w*";

    private static final Pattern TO_LINE_REGEXP = Pattern.compile("([A-Z]+)");

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
     *
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


    private static final String LINE_CHAR="_";

    /**
     * 驼峰格式的字符串转下划线
     *
     * @param str
     * @return
     */
    public static String humpToLine(String str) {
        if (!StringUtils.hasText(str)) {
            return str;
        }
//        //@link http://ifeve.com/google-guava/
//        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
        StringBuffer text = new StringBuffer(str);
        Matcher matcher = TO_LINE_REGEXP.matcher(text);
        while (matcher.find()) {
            String replaceText = MessageFormat.format("{0}{1}", LINE_CHAR, text.substring(matcher.start(0), matcher.end(0)));
            text.replace(matcher.start(0), matcher.end(0), replaceText.toLowerCase());
            matcher = TO_LINE_REGEXP.matcher(text);
        }
        String result = text.toString();
        if (result.startsWith(LINE_CHAR)) {
            return result.substring(1);
        }
        return result;
    }

}
