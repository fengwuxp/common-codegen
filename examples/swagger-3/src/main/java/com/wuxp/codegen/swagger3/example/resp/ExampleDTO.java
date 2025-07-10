package com.wuxp.codegen.swagger3.example.resp;

import lombok.Data;

@Data
public class ExampleDTO {

    Integer querySize;

    Integer queryPage;

    private String name;

    private String keyword;

    private TestMethodDTO method;
}
