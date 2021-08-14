package com.wuxp.codegen.swagger2.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

public class BaseController<T> {

    @RequestMapping("/test1")
    @ApiIgnore
    public void test1() {

    }


    @RequestMapping("/test2")
    public void test2(T t) {

    }
}
