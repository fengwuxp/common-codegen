package com.wuxp.codegen.server;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wuxp
 */
@Slf4j
@SpringBootApplication
public class LoongCodegenApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoongCodegenApplication.class, args);
        log.info("loong codegen server start success");
    }
}
