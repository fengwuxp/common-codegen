package com.wuxp.codegen.swagger3.example.maven.resp;

import lombok.Data;

@Data
public class ServiceResponse<T> {


  String message;


  Integer code;


  T data;
}
