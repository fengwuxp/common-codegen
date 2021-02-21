package com.wuxp.codegen.server.repositories;

import com.wuxp.codegen.server.CodegenJpaAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class CodegenJpaAuditorAwareConfig {

    @Bean
    public AuditorAware<String> codegenJpaAuditorAware() {
        return new CodegenJpaAuditorAware();
    }
}
