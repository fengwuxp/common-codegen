package com.wuxp.codegen.swagger3.example.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger3Config {

    //    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("basicScheme", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
//                .info(new Info()
//                        .title("example API")
//                        .version("1.0.0")
//                        .license(new License()
//                                .name("example")
//                                .url("http://example.com")));
//    }
    @Bean
    public GroupedOpenApi usersGroup() {
        return GroupedOpenApi.builder().group("example")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    operation.addSecurityItem(new SecurityRequirement().addList("basicScheme"));
                    return operation;
                })
                .addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Example API").version("1.0.0")))
                .packagesToScan("com.wuxp.codegen.swagger3.example")
                .build();
    }
}
