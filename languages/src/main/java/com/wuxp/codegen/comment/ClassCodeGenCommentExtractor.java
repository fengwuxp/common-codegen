package com.wuxp.codegen.comment;

import com.wuxp.codegen.core.CodeGenCommentExtractor;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;

import java.lang.annotation.ElementType;
import java.text.MessageFormat;

/**
 * @author wuxp
 */
public class ClassCodeGenCommentExtractor implements CodeGenCommentExtractor {

    private final ElementType elementType;

    public ClassCodeGenCommentExtractor(ElementType elementType) {
        this.elementType = elementType;
    }


    @Override
    public String toComment(Class<?> clazz) {
        String simpleName = clazz.equals(JavaArrayClassTypeMark.class) ? "数组" : clazz.getSimpleName();
        return MessageFormat.format("{0}在java中的类型为：{1}", getTypeDesc(), simpleName);
    }

    private String getTypeDesc() {
        switch (elementType) {
            case METHOD:
                return "返回值";
            case FIELD:
                return "字段";
            case PARAMETER:
                return "参数";
            default:
                return "";
        }
    }


}
