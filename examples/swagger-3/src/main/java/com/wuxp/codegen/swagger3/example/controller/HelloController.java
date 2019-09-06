package com.wuxp.codegen.swagger3.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HelloController {

    /**
     * 标记忽略
     * @return
     */
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @Operation(description="Documented with OpenAPI v3 annotations")
    public String index() {
        return "Hello World";
    }

}
