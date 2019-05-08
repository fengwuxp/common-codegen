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


    public static final LanguageDescription JAVA = new LanguageDescription("java", "java");

    public static final LanguageDescription JAVA_ANDROID = new LanguageDescription("java", "java","android");

    /**
     * 名称
     */
    private String name;

    /**
     * 文件后缀名称
     */
    private String suffixName;


    /**
     * 模板目录
     */
    private String templateDir;


    /**
     * 代码生成类型
     */
    private CodeGenType codeGenType = CodeGenType.FEIGN;


    public LanguageDescription(String name, String suffixName) {
        this.name = name;
        this.suffixName = suffixName;
        this.templateDir = name;
    }

    public LanguageDescription(String name, String suffixName, String templateDir) {
        this.name = name;
        this.suffixName = suffixName;
        this.templateDir = templateDir;
    }
}
