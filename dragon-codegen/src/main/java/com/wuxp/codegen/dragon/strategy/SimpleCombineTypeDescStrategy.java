package com.wuxp.codegen.dragon.strategy;

import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.wuxp.codegen.helper.GrabGenericVariablesHelper.GENERIC_PLACEHOLDER;

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

        String metaName = genClassMeta.getName();
        if (metaName.equals(CommonCodeGenClassMeta.ARRAY_TYPE_NAME)) {
            //数组
            CommonCodeGenClassMeta[] typeVariables = genClassMeta.getTypeVariables();
            return metaName.replace("T", typeVariables[0].getName());
        }


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
            log.warn("泛型描述不存在，基础类型{}，期望获取泛型的类型{}", genClassMeta.getName(), finallyGenericDescription);

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
    public String combineTypes(List<String> names) {
        int size = names.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return names.get(0);
        }

        //使用两两合并的方式处理
        //例如 ：List<T>,Map<K,PageInfo<T>>,String,User
        // ==> List<Map<K,PageInfo<T>>>,String,User
        // ==> List<Map<String,PageInfo<T>>>,User
        // ==> List<Map<String,PageInfo<User>>>

        // 如果是Map多其他具有多个泛型变量的描述 使用从左向右的方式递归合并


        Stack<String> typeVariableTempStack = new Stack<>();
        Stack<String> typeVariableStack = new Stack<>();
        Collections.reverse(names);
        typeVariableTempStack.addAll(names);
        while (!typeVariableTempStack.isEmpty()) {
            String name = typeVariableTempStack.pop();
            typeVariableStack.push(name);
            List<String> descriptors = GrabGenericVariablesHelper.matchGenericDescriptorPlaceholders(name);
            if (descriptors.isEmpty()) {
                // 如果不存在，合并前面的泛型变量
                String typeVariableName = this.replaceTypeVariableName(typeVariableStack);
                if (typeVariableName.equals(name)) {
                    // 合并结束
                    return typeVariableName;
                }
                typeVariableTempStack.add(typeVariableName);
            }
        }

        return null;

    }


    /**
     * 替换泛型变量
     *
     * @param typeVariableNames
     * @return
     */
    private String replaceTypeVariableName(Stack<String> typeVariableNames) {

        Stack<String> temp = new Stack<>();

        while (!typeVariableNames.isEmpty()) {
            String typeNme = typeVariableNames.pop();
            temp.push(typeNme);
            if (GrabGenericVariablesHelper.existGenericDescriptorPlaceholders(typeNme)) {
                String genericDescriptor = this.replaceGenericDescriptor(temp);
                temp.push(genericDescriptor);
            }
        }
        return temp.pop();
    }

    /**
     * 替换泛型描述符
     *
     * @param typeVariableNames
     * @return
     */
    private String replaceGenericDescriptor(Stack<String> typeVariableNames) {
        String typeVariableName = typeVariableNames.pop();
        List<String> descriptors = GrabGenericVariablesHelper.matchGenericDescriptorPlaceholders(typeVariableName);
        if (descriptors.isEmpty()) {
            // 没有有泛型描述变量
            throw new RuntimeException("match generic descriptor failure");
        }
        typeVariableName = GrabGenericVariablesHelper.tryConverterTypeVariableToPlaceholder(typeVariableName);
        List<String> names = new ArrayList<>();
        while (!typeVariableNames.isEmpty()) {
            names.add(typeVariableNames.pop());
        }
        int size = names.size();
        int i = descriptors.size() - size;
        while (--i >= 0) {
            // 补全
            names.add(GENERIC_PLACEHOLDER);
        }
        return this.replaceGenericDescCode(typeVariableName, descriptors, names);
    }


    /**
     * 替换泛型描述代码
     *
     * @param genericDescription
     * @param descriptors
     * @param typeVariables
     * @return
     */
    private String replaceGenericDescCode(String genericDescription, List<String> descriptors, List<String> typeVariables) {
        //精确匹配<T>
//        return genericDescription.replaceAll("<" + String.join(",", descriptors) + ">", "<" + replaceName + ">");

        for (String typeVariable : typeVariables) {

            genericDescription = genericDescription.replaceFirst(GENERIC_PLACEHOLDER, typeVariable);
        }
        return genericDescription;
    }


}
