package com.wuxp.codegen.server.repositories;

import com.wuxp.codegen.server.entities.CodeVersionControlConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@Import(CodegenJpaAuditorAwareConfig.class)
@DataJpaTest(properties = {"spring.profiles.active=test"})
class SourceCodeRepositoryRepositoryTestConfiguration {

    @Autowired
    private CodeVersionControlConfigRepository codeVersionControlConfigRepository;

    @Test
    void testFinAll() {
        List<CodeVersionControlConfig> list = codeVersionControlConfigRepository.findAll();
        Assertions.assertTrue(list.isEmpty());
    }
}
