package com.wuxp.codegen.spring.model;


import lombok.Getter;

@Getter
public enum DtoObjectType {

    CREATE("create"),

    UPDATE("edit"),

    DELETED("delete"),

    QUERY("query"),

    RESP_INFO("");

    private final String placeholder;

    DtoObjectType(String placeholder) {
        this.placeholder = placeholder;
    }
}
