package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.LoongCodegenProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = {
        LoongCodegenProperties.class,
        JGitSourcecodeRepositoryTest.JGitSourcecodeRepositoryConfig.class
})
@TestPropertySource("classpath:application-test.properties")
@ConfigurationPropertiesScan("com.wuxp.codegen")
class JGitSourcecodeRepositoryTest {

    @MockitoBean
    private SourcecodeRepository sourcecodeRepository;

    @Test
    void testDownload() {
        when(sourcecodeRepository.checkout(anyString())).thenReturn("mock");
        String filepath = sourcecodeRepository.checkout("common-codegen");
        Assertions.assertEquals("mock", filepath);
    }

    @Test
    void testGetBranchList() {
        when(sourcecodeRepository.getBranchList(anyString())).thenReturn(Collections.emptyList());
        List<String> branchList = sourcecodeRepository.getBranchList("common-codegen");
        Assertions.assertTrue(branchList.isEmpty());
    }


    @Configuration
    public static class JGitSourcecodeRepositoryConfig {

        @Bean
        public SourcecodeRepository gitSourcecodeRepository(LoongCodegenProperties config) {
            return new JGitSourcecodeRepository(config.getRepositories().get(0));
        }
    }


}