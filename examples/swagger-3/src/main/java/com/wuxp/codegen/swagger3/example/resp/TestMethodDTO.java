package com.wuxp.codegen.swagger3.example.resp;


import lombok.Data;

import java.util.Date;


@Data
public class TestMethodDTO {

    private String name;

    private Short age;


    private Boolean flag;


    private Date birthDay;

    private TestMethodDTO example;
}
