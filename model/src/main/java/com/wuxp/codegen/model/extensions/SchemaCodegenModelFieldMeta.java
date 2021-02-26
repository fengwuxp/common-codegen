package com.wuxp.codegen.model.extensions;


import lombok.Data;

import java.io.Serializable;

/**
 * 字段描述
 *
 * @author wuxp
 */
@Data
public class SchemaCodegenModelFieldMeta implements Serializable {

    private static final long serialVersionUID = -5775288658506713812L;

    private String name;

    private String type;

    private boolean required;

    private String[] enumFiledValues;

    /**
     * 是否为数组类型
     * @return
     */
    public boolean isArray() {
        return type.endsWith("[]");
    }

    /**
     * 是否为引用其他的类型
     * @return
     */
    public boolean isRef() {
        return type.startsWith("$");
    }
}