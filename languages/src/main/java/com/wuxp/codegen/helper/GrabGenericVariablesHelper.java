package com.wuxp.codegen.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 抓取字符串中的泛型变量
 *
 * @author wuxp
 */
@Slf4j
public final class GrabGenericVariablesHelper {

    /**
     * 提取泛型变量的正则表达式
     */
    private static final String GENERIC_DESCRIPTOR = "<(.*?)>$+";

    private static final Pattern GENERIC_PATTERN = Pattern.compile(GENERIC_DESCRIPTOR);

    private GrabGenericVariablesHelper() {
        throw new AssertionError();
    }

    /**
     * 抓取泛型描述符列表 例如：Map<K,V> ==>[K,V]
     *
     * @param genericDescription 泛型描述
     * @return 泛型变量列表
     */
    public static List<String> matchGenericDescriptors(String genericDescription) {
        if (!StringUtils.hasText(genericDescription) || !genericDescription.contains("<")) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(grabGenericVariables(genericDescription));
    }

    /**
     * 是否存在泛型描述符
     *
     * @param genericDescription 泛型描述
     * @return
     */
    public static boolean existGenericDescriptors(String genericDescription) {
        return !matchGenericDescriptors(genericDescription).isEmpty();
    }


    /**
     * 抓取泛型变量
     *
     * @param genericDescription 泛型描述
     * @return 泛型变量列表
     */
    private static List<String> grabGenericVariables(String genericDescription) {
        List<String> list = new ArrayList<>();
        if (isGenericVariable(genericDescription)) {
            list.add(genericDescription);
            return list;
        }
        if (log.isDebugEnabled()) {
            log.debug("genericDescription--> {}", genericDescription);
        }

        Matcher matcher = GENERIC_PATTERN.matcher(genericDescription);

        if (matcher.find()) {
            String group = matcher.group();
            //去除最外层的2个尖括号
            String nextGenericDescriptor = group.substring(1, group.length() - 1);

            if (nextGenericDescriptor.endsWith(">")) {
                //判断逗号是否存在
                int i1 = nextGenericDescriptor.indexOf(",");
                int i2 = nextGenericDescriptor.indexOf("<");
                if (i1 == -1 || i2 < i1) {
                    //若 第一个','号在第一个'<'范围中，则继续匹配
                    list.addAll(GrabGenericVariablesHelper.grabGenericVariables(nextGenericDescriptor));
                }
                if (i1 > -1 && ((i2 == -1 && i1 > 0) || i1 < i2)) {
                    //若 第一个','号在第一个'<'范围外，则尝试分割匹配
                    String[] strings = {nextGenericDescriptor.substring(0, i1), nextGenericDescriptor.substring(i1 + 1)};
                    list.addAll(Arrays.stream(strings)
                            .map(GrabGenericVariablesHelper::grabGenericVariables)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList()));
                }
            } else {
                List<String> collect = Arrays.stream(nextGenericDescriptor
                                .split(","))
                        .filter(StringUtils::hasText)
                        .map(GrabGenericVariablesHelper::grabGenericVariables)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
                list.addAll(collect);
            }

        }

        return list;
    }

    /**
     * 是否为泛型变量
     *
     * @param genericDescriptor 泛型描述符
     * @return if <code>true</code> 是
     */
    private static boolean isGenericVariable(String genericDescriptor) {

        if (genericDescriptor.length() == 1) {
            char c = genericDescriptor.toCharArray()[0];
            //是单个的大写字母
            if (c >= 'A' && c <= 'Z') {
                return true;
            }
        }
        return false;
    }
}
