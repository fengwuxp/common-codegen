package com.wuxp.codegen.types;

import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * dart FullType 合并
 *
 * @author wxup
 */
@Slf4j
public class DartFullTypeCombineTypeDescStrategy implements CombineTypeDescStrategy {


    private final CombineTypeDescStrategy combineTypeDescStrategy = new SimpleCombineTypeDescStrategy();


    @Override
    public String combine(CommonCodeGenClassMeta[] classMetas) {
        //取泛型描述
        String genericDesc = this.combineTypeDescStrategy.combine(classMetas);
        String fullTypeCode = this.getFullTypeCode(genericDesc);
        log.info("合并dart FullType {}", fullTypeCode);

        return fullTypeCode;
    }


    /**
     * 获取 FullType Code
     * <p>
     * 替换示例 List<Map<String, List<User>>> ==> FullType(List<Map<String, List<User>>>) FullType(List [FullType(Map<String, List<User>>]))
     * FullType(List [FullType(Map[FullType(String), FullType(List<User>>)])) FullType(List,[FullType(Map,[ FullType(String),FullType(
     * List,[FullType(User)])])])
     *
     * @param originalGenericDesc 泛型描述 例如 Map<String,String>
     * @return fullType 类型表达式（支持泛型）
     */
    private String getFullTypeCode(String originalGenericDesc) {
        String fullTypeFormat = "FullType({0})";
        String[] fullTypeGenericParts = generateFullTypeGenericParts(originalGenericDesc, fullTypeFormat);
        if (ObjectUtils.isEmpty(fullTypeGenericParts)) {
            return null;
        }
        return MessageFormat.format(fullTypeFormat, originalGenericDesc.replaceAll(fullTypeGenericParts[0], fullTypeGenericParts[1]))
                .replace("<", ",[")
                .replace(">", "]");
    }

    /**
     * 生成 Built FullType 的泛型描述部分
     *
     * @param originalGenericDesc 原始的泛型描述内容
     * @param fullTypeFormat      String format pattern
     * @return [原始泛型描述 <T> 中的 T,FullType 泛型的表达式]
     */
    private String[] generateFullTypeGenericParts(String originalGenericDesc, String fullTypeFormat) {
        Stack<String> genericNames = splitAllGenericNames(originalGenericDesc);
        if (genericNames.empty()) {
            return new String[0];
        }

        Stack<String[]> tempStack = new Stack<>();
        while (!genericNames.empty()) {
            String genericName = genericNames.pop();
            String typeStr = String.valueOf(genericName);
            if (!tempStack.empty()) {
                String[] items = tempStack.pop();
                typeStr = typeStr.replaceAll(items[0], items[1]);
            }
            String type;
            int leftArrowIndex = typeStr.indexOf("<");
            if ((typeStr.indexOf(",") < leftArrowIndex) || leftArrowIndex < 0) {
                type = Arrays.stream(typeStr.split(","))
                        .map(text -> {
                            if (text.startsWith("FullType")) {
                                return text;
                            }
                            return MessageFormat.format(fullTypeFormat, text);
                        })
                        .collect(Collectors.joining(","));
            } else {
                type = MessageFormat.format(fullTypeFormat, typeStr);
            }
            tempStack.push(new String[]{genericName, type});
        }
        if (tempStack.isEmpty()) {
            return new String[0];
        }
        return tempStack.pop();
    }

    private Stack<String> splitAllGenericNames(String originalGenericDesc) {
        String value = String.valueOf(originalGenericDesc);
        Stack<String> typeStacks = new Stack<>();
        // 分割出所有的 类型
        int start = -1;
        while ((start = value.indexOf("<")) > -1) {
            value = value.substring(start + 1, value.length() - 1);
            // 存在复合类型的key 不支持
            Assert.isTrue(!value.contains(">,"), "不支持存在复杂类型的 key, originalGenericDesc = " + originalGenericDesc);
            typeStacks.push(value);
        }
        return typeStacks;
    }
}
