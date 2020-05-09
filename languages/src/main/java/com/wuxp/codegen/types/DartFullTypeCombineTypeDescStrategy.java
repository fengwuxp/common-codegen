package com.wuxp.codegen.types;

import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;

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


    private CombineTypeDescStrategy combineTypeDescStrategy = new SimpleCombineTypeDescStrategy();


    @Override
    public String combine(CommonCodeGenClassMeta[] codeGenClassMetas) {

        //取泛型描述
        String genericDesc = this.combineTypeDescStrategy.combine(codeGenClassMetas);
        String fullTypeCode = this.getFullTypeCode(genericDesc);
        log.info("合并dart FullType {}", fullTypeCode);

        return fullTypeCode;
    }


    /**
     * 获取 FullType Code
     * <p>
     * 替换示例
     * List<Map<String, List<User>>>
     * ==>
     * FullType(List<Map<String, List<User>>>)
     * FullType(List [FullType(Map<String, List<User>>]))
     * FullType(List [FullType(Map[FullType(String), FullType(List<User>>)]))
     * FullType(List,[FullType(Map,[ FullType(String),FullType( List,[FullType(User)])])])
     *
     * @param originalGenericDesc 泛型描述 例如 Map<String,String>
     * @return
     */
    private String getFullTypeCode(String originalGenericDesc) {

//            originalGenericDesc = "BuiltMap<PageInfo<User>,BuiltList<PageInfo<User>>>";
        String value = String.valueOf(originalGenericDesc);
        Stack<String> typeStacks = new Stack<>();

        //分割出所有的 类型
        int start = -1;
        while ((start = value.indexOf("<")) > -1) {
            value = value.substring(start + 1, value.length() - 1);
            if (value.indexOf(">,") > 0) {
                // 存在复合类型的key 不支持
                return null;
            }
            typeStacks.push(value);
        }

        String fullTypeFormat = "FullType({0})";
        Stack<String[]> tempStack = new Stack<>();
        while (!typeStacks.empty()) {
            String key = typeStacks.pop();
            String typeStr = new String(key);
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
            tempStack.push(new String[]{key, type});
        }
        if (tempStack.isEmpty()) {
            return null;
        }
        String[] items = tempStack.pop();
        String returnValue = MessageFormat.format(fullTypeFormat, originalGenericDesc.replaceAll(items[0], items[1]))
                .replaceAll("\\<", ",[")
                .replaceAll("\\>", "]");
        return returnValue;
    }
}
