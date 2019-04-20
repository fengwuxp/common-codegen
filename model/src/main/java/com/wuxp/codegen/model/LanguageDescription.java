package com.wuxp.codegen.model;

import com.wuxp.codegen.model.enums.CodeGenType;
import lombok.Data;

/**
 * 语言名称列表
 */
@Data
public final class LanguageDescription {

    public static final LanguageDescription TYPESCRIPT = new LanguageDescription("typescript", "ts");

    public static final LanguageDescription DART = new LanguageDescription("dart", "dart");

    /**
     * 名称
     */
    private String name;

    /**
     * 文件后缀名称
     */
    private String suffixName;


    /**
     * 代码生成类型
     */
    private CodeGenType codeGenType = CodeGenType.FEIGN;


    public LanguageDescription(String name, String suffixName) {
        this.name = name;
        this.suffixName = suffixName;
    }
}
