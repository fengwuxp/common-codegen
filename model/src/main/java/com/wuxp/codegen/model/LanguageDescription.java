package com.wuxp.codegen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 语言名称列表
 *
 * @author wuxp
 */
@AllArgsConstructor
@Getter
public enum LanguageDescription {

    /**
     * typescript
     */
    TYPESCRIPT("typescript", "ts"),

    /**
     * dart
     */
    DART("dart", "dart"),

    /**
     * java
     */
    JAVA("java", "java"),

    /**
     * android
     */
    JAVA_ANDROID("java", "java"),
    ;

    /**
     * 名称
     */
    private final String name;

    /**
     * 文件后缀名称
     */
    private final String suffixName;


}
