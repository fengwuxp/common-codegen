package com.wuxp.codegen.swagger2.example.controller;


import com.wuxp.codegen.swagger2.example.domain.Order;
import com.wuxp.codegen.swagger2.example.domain.User;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * list test（源码注释）
 *
 * @author wuxp
 */
@Api("list tst")
@RestController
@RequestMapping(value = "/list")
public class ListParamsTestController {

    @PostMapping
    public String test1(@RequestBody List<User> users) {
        return "";
    }

    @GetMapping("test_2")
    public String test2(User[] users) {
        return "";
    }

    @GetMapping("test_3")
    public String test3(Map<String, Order> users) {
        return "";
    }

    @PostMapping("test_4")
    public String test4(@RequestBody Set<User> users) {
        return "";
    }

    @PostMapping("test_5")
    public String test5(@RequestBody Collection<User> users) {
        return "";
    }

    @PostMapping("test_6")
    public String test6(@RequestBody LinkedHashSet<User> users) {
        return "";
    }

}
