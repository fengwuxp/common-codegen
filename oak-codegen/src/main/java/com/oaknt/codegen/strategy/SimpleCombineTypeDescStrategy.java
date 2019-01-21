package com.oaknt.codegen.strategy;

import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 简单的类型合并描述
 */
@Slf4j
public class SimpleCombineTypeDescStrategy implements CombineTypeDescStrategy {




    @Override
    public String combine(CommonCodeGenClassMeta[] codeGenClassMetas) {

        if (codeGenClassMetas == null || codeGenClassMetas.length == 0) {
            return null;
        }
        int length = codeGenClassMetas.length;
        CommonCodeGenClassMeta genClassMeta = codeGenClassMetas[0];
        String finallyGenericDescription = genClassMeta.getFinallyGenericDescription();
        if (length == 1) {
            if (!StringUtils.hasText(finallyGenericDescription)) {
                return genClassMeta.getName();
            }
            return finallyGenericDescription;
        }


        //存在泛型
        String genericDescription = genClassMeta.getFinallyGenericDescription();
        if (!StringUtils.hasText(genericDescription)) {
            log.warn("泛型描述不存在，基础类型{}，期望获取泛型的类型", genClassMeta.getName(), finallyGenericDescription);

            return finallyGenericDescription;
        }

        genericDescription = this.combineTypes(Arrays.stream(codeGenClassMetas)
                .map(codeGenClassMeta -> this.combine(new CommonCodeGenClassMeta[]{codeGenClassMeta}))
                .collect(Collectors.toList()));

        log.debug("生成的泛型描述为：{}", genericDescription);

        return genericDescription;
    }


    /**
     * 合并类型描述
     *
     * @param names
     * @return
     */
    private String combineTypes(List<String> names) {
        int size = names.size();
        if (size == 0) {
            return null;
        }

        //使用两两合并的方式处理
        //例如 ：List<T>,Map<K,PageInfo<T>>,String,User
        // ==> List<Map<K,PageInfo<T>>>,String,User
        // ==> List<Map<String,PageInfo<T>>>,User
        // ==> List<Map<String,PageInfo<User>>>

        String typeNme = names.get(0);

        //泛型描述列表 例如： T、K、V
        List<String> descriptors = GrabGenericVariablesHelper.matchGenericDescriptors(typeNme);

        //存在泛型描述
        if (descriptors.size() > 0) {
            if (size < 2) {
                throw new RuntimeException("合并泛型描述异常，names = " + String.join(",", names));
            }

            //合并替换后的泛型描述
            String genericDescriptor = this.replaceGenericDescriptor(descriptors, names.subList(1, names.size()));
            if (genericDescriptor == null) {
                return typeNme;
            }
            typeNme = replaceGenericDescCode(typeNme, descriptors, genericDescriptor);
        }

        if (names.size() > 1) {
            names = names.subList(1, names.size());
        }

        names.set(0, typeNme);

        if (names.size() > 1) {
            typeNme = this.combineTypes(names);
        }
        return typeNme;
    }

    /**
     * 替换泛型描述符号
     *
     * @param genericDescriptors
     * @param names
     * @return
     */
    private String replaceGenericDescriptor(List<String> genericDescriptors, List<String> names) {
        if (genericDescriptors.size() > names.size()) {
            return null;
        }

        List<String> result = new ArrayList<>();

        for (int i = 0; i < genericDescriptors.size(); i++) {
            String genericDescription = genericDescriptors.get(i);
            List<String> descriptors = GrabGenericVariablesHelper.matchGenericDescriptors(genericDescription);
            int size = descriptors.size();
            if (size > 0) {
                //有泛型描述符号
                String descriptor = this.replaceGenericDescriptor(descriptors, names.subList(i, names.size()));
                result.add(replaceGenericDescCode(genericDescription, descriptors, descriptor));
            } else {
                result.add(names.get(i));
            }
        }
        return String.join(",", result);

    }

    /**
     * 替换泛型描述代码
     *
     * @param genericDescription
     * @param descriptors
     * @param replaceName
     * @return
     */
    private String replaceGenericDescCode(String genericDescription, List<String> descriptors, String replaceName) {
        //精确匹配<T>
        return genericDescription.replaceAll("<" + String.join(",", descriptors) + ">", "<" + replaceName + ">");
    }




}