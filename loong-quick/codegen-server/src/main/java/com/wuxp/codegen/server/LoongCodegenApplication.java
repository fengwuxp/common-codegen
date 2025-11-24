package com.wuxp.codegen.server;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author wuxp
 */
@Slf4j
@EnableJpaRepositories(basePackages = {"com.wuxp.codegen.server.repositories"})
@EntityScan("com.wuxp.codegen.server.entities")
@EnableJpaAuditing(auditorAwareRef = "codegenJpaAuditorAware")
@SpringBootApplication(scanBasePackages = {"com.wuxp.codegen.server"})
public class LoongCodegenApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoongCodegenApplication.class, args);
        log.info("loong codegen server start success");
    }
}
