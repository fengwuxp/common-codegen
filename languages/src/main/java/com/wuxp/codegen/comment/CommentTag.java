package com.wuxp.codegen.comment;

/**
 * 注释标记类型枚举
 *
 * @author wuxp
 * @see com.github.javaparser.javadoc.JavadocBlockTag.Type
 */
public enum CommentTag {

    /**
     * 参数
     */
    PARAMETER("param"),

    /**
     * 返回值
     */
    RETURN("return")
    ;

    String tagName;

    CommentTag(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }
}
