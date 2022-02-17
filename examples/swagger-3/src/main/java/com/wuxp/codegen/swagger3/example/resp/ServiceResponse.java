package com.wuxp.codegen.swagger3.example.resp;

import lombok.Data;

@Data
public class ServiceResponse<T> {

    String message;

    Integer code;

    T data;

    public T getData() {
        return data;
    }
}
