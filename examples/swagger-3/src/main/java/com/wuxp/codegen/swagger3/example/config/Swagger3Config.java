package com.wuxp.codegen.swagger3.example.config;


import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger3Config {

    @Bean
    public GroupedOpenApi usersGroup() {
        return GroupedOpenApi.builder().group("example")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    operation.addSecurityItem(new SecurityRequirement().addList("basicScheme"));
                    return operation;
                })
                .addOpenApiCustomizer(openApi -> openApi.info(openApi.getInfo().title("Example API").version("1.0.0")))
                .packagesToScan("com.wuxp.codegen.swagger3.example")
                .build();
    }
}
