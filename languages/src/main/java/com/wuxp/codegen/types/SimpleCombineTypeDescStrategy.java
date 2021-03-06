package com.wuxp.codegen.types;

import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import static com.wuxp.codegen.model.CommonCodeGenClassMeta.ARRAY_TYPE_GENERIC_DESCRIPTION;


/**
 * 简单的类型合并描述
 *
 * @author wuxp
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
//    CommonCodeGenClassMeta lastCodeMeta = codeGenClassMetas[codeGenClassMetas.length - 1];
//    String metaName = genClassMeta.getName();
//        if (lastCodeMeta.getName().endsWith(CommonCodeGenClassMeta.ARRAY_TYPE_NAME_PREFIX)) {
//            //合并数组的类型
//            CommonCodeGenClassMeta[] typeVariables = genClassMeta.getTypeVariables();
//            return metaName.replace("T", typeVariables[0].getName());
//        }

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

        if (log.isDebugEnabled()) {
            log.debug("生成的泛型描述为：{}", genericDescription);
        }

        return genericDescription;
    }


    /**
     * 合并泛型描述
     *
     * <p>
     * example: names.add("Promise<K>"); names.add("Map<K,V>"); names.add("PageInfo<T>"); names.add("User"); names.add("List<T>");
     * names.add("Page<T>"); names.add("Member"); strategy.combineTypes(names) 输出： Promise<Map<PageInfo<User>,List<Page<Member>>>>
     * </p>
     *
     * @param names 泛型描述列表
     * @return 完整的泛型描述
     */
    public String combineTypes(List<String> names) {
        int size = names.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return names.get(0);
        }

        // 使用从后向前合并的方式
        //例如 ：List<T>,Map<K,PageInfo<T>>,String,User
        // ==> List<Map<K,PageInfo<T>>>,String,User
        // ==> List<Map<String,PageInfo<T>>>,User
        // ==> List<Map<String,PageInfo<User>>>

        Stack<String> typeVariableTempStack = new Stack<>();
        typeVariableTempStack.addAll(names);
        // 用来保存合并好的或没有泛型的变量名称
        Stack<String> finallyTypeVariableNameStack = new Stack<>();

        while (!typeVariableTempStack.isEmpty()) {
            String typeVariableName = typeVariableTempStack.pop();
            finallyTypeVariableNameStack.push(typeVariableName);
            if (GrabGenericVariablesHelper.existGenericDescriptors(typeVariableName)) {
                // 存在泛型变量
                String variableName = this.replaceGenericDescriptor(finallyTypeVariableNameStack);
                // 加入临时栈
                typeVariableTempStack.push(variableName);
                if (typeVariableTempStack.size() == 1) {
                    return typeVariableTempStack.pop();
                }
            } else {
                // 不存在

            }
        }

        return null;

    }


    /**
     * 替换泛型描述符
     *
     * @param typeVariableNames
     * @return
     */
    private String replaceGenericDescriptor(final Stack<String> typeVariableNames) {
        String typeVariableName = typeVariableNames.pop();
        List<String> descriptors = GrabGenericVariablesHelper.matchGenericDescriptors(typeVariableName);
        if (descriptors.isEmpty()) {
            // 没有有泛型描述变量
            throw new RuntimeException("match generic descriptor failure");
        }
        if (typeVariableNames.size() < descriptors.size()) {
            // 类型变量的长度小于泛型描述符
//            throw new RuntimeException("type variable name size error");
            log.error("type variable name size error");
            return "";
        }
        List<String> names = new ArrayList<>();
        descriptors.forEach(s -> {
            names.add(typeVariableNames.pop());
        });

        return this.replaceGenericDescCode(typeVariableName, descriptors, names);
    }


    /**
     * 替换泛型描述代码
     *
     * @param genericDescription 泛型描述，例如： Map<K,V>
     * @param descriptors        泛型变量列表，例如：[K,V]
     * @param typeVariables      泛型变量的值，例如：[String,String]
     * @return 完整的泛型描述，例如：Map<String,String>
     */
    private String replaceGenericDescCode(String genericDescription, List<String> descriptors, List<String> typeVariables) {

        if (ARRAY_TYPE_GENERIC_DESCRIPTION.equals(genericDescription)) {
            // 处理数组的泛型合并
            return typeVariables.get(0) + "[]";
        }
        //精确匹配<T>
        return genericDescription.replaceAll("<" + String.join(",", descriptors) + ">", "<" + String.join(",", typeVariables) + ">");

    }


}
