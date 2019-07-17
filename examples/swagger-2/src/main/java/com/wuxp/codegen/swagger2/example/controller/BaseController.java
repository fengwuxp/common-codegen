package com.wuxp.codegen.swagger2.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class BaseController<T> {


    @RequestMapping
    public void test1() {

    }


    @RequestMapping
    public void test2(T t) {

    }
}
