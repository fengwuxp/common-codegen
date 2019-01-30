package com.wuxp.codegen.dragon.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 抓取字符串中的泛型变量
 */
public final class GrabGenericVariablesHelper {

    private static final String GENERIC_DESCRIPTOR = "\\<+(.*?)\\>$+";

    /**
     * 抓取泛型描述符列表 例如：Map<K,V> ==>[K,V]
     *
     * @param genericDescription
     * @return
     */
    public static List<String> matchGenericDescriptors(String genericDescription) {
        Pattern pattern = Pattern.compile(GENERIC_DESCRIPTOR);

        Matcher matcher = pattern.matcher(genericDescription);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (group.endsWith(">>")) {
                //不是最里面的泛型
                list.addAll(GrabGenericVariablesHelper.matchGenericDescriptors(group));
            } else {
                List<String> c = Arrays.asList(group.split(","));
                list.addAll(c);
            }

        }
        return Collections.unmodifiableList(list);
    }
}
