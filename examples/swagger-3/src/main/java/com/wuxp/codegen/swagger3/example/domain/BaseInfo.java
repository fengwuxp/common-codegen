package com.wuxp.codegen.swagger3.example.domain;

import lombok.Data;

@Data
public class BaseInfo<I, T> {

    protected I id;

    protected T data;

    protected BaseExample example;

    public enum BaseExample {

        A;
    }
}
