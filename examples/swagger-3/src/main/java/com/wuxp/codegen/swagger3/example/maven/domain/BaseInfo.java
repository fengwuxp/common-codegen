package com.wuxp.codegen.swagger3.example.maven.domain;

import lombok.Data;

@Data
public class BaseInfo<ID, T> {

    protected ID id;

    protected T data;

    protected BaseExample example;

    public enum BaseExample {

        A;
    }
}
