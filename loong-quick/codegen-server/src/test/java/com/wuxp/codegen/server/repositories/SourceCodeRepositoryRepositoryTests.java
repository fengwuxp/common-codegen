package com.wuxp.codegen.server.repositories;

import com.wuxp.codegen.server.entities.CodeVersionControlConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@ContextConfiguration(classes = {CodegenJpaAuditorAwareConfig.class})
@EnableJpaRepositories(basePackages = {"com.wuxp.codegen.server.repositories"})
@EntityScan("com.wuxp.codegen.server.entities")
@ActiveProfiles("test")
class SourceCodeRepositoryRepositoryTests {

    @Autowired
    private CodeVersionControlConfigRepository codeVersionControlConfigRepository;

    @Test
    void testFinAll() {
        List<CodeVersionControlConfig> list = codeVersionControlConfigRepository.findAll();
        Assertions.assertTrue(list.isEmpty());
    }
}
