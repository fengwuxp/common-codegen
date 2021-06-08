package com.wuxp.codegen.model.enums;

/**
 * 访问权限控制
 * @author wuxp
 */
public enum AccessPermission {


    PRIVATE("private"),

    PUBLIC("public"),

    PROTECTED("protected"),

    DEFAULT("");


    private String value;

    AccessPermission(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isPublic(){
        return PUBLIC.equals(this);
    }
    public boolean isPrivate(){
        return PRIVATE.equals(this);
    }
}
