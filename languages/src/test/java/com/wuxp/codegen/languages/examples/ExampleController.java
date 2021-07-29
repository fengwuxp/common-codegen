package com.wuxp.codegen.languages.examples;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
public class ExampleController {

    @GetMapping("/say_hello")
    public String sayHello(String name) {
        return "hello " + name;
    }
}
