package com.oaknt.codegen.strategy;

import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的类型合并描述
 */
@Slf4j
public class SimpleCombineTypeDescStrategy implements CombineTypeDescStrategy {

    protected Map<Class<?>, CommonCodeGenClassMeta> typeMap;

    @Override
    public String combine(CommonCodeGenClassMeta[] codeGenClassMetas) {

        if (codeGenClassMetas == null || codeGenClassMetas.length == 0) {
            return null;
        }
        int length = codeGenClassMetas.length;
        CommonCodeGenClassMeta genClassMeta = codeGenClassMetas[0];
        String genClassMetaName = genClassMeta.getName();
        if (length == 1) {
            if (!StringUtils.hasText(genClassMetaName)) {
                return genClassMeta.getFinallyGenericDescription();
            }
            return genClassMetaName;
        }

        //存在泛型
        String genericDescription = genClassMeta.getFinallyGenericDescription();
        if (!StringUtils.hasText(genericDescription)) {
            throw new RuntimeException("泛型描述不存在，" + genClassMetaName);
        }
        List<String> genericDescriptors = this.matchGenericDescriptors(genericDescription);

        int i = 1;

        for (String d : genericDescriptors) {
            if (i == length) {
                break;
            }
            String name = this.combine(Arrays.asList(codeGenClassMetas)
                    .subList(i, length).toArray(new CommonCodeGenClassMeta[]{}));
            //TODO　Map<K,V>
            //精确匹配<T>
            genericDescription = genericDescription.replaceAll("<"+d+">", "<"+name+">");
            i++;
        }

        log.debug("生成的泛型描述为：{}", genericDescription);

        return genericDescription;
    }


    protected static final String GENERIC_DESCRIPTOR = "\\<+(.*?)\\>+";

    /**
     * 抓取泛型描述符列表 例如：Map<K,V> ==>[K,V]
     *
     * @param genericDescription
     * @return
     */
    private List<String> matchGenericDescriptors(String genericDescription) {
        Pattern pattern = Pattern.compile(GENERIC_DESCRIPTOR);

        Matcher matcher = pattern.matcher(genericDescription);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group(1));
        }
        return list;
    }

}