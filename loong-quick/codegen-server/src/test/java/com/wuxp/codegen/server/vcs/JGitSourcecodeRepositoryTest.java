package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.SourcecodeRepositoryPropertiesConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = {
        SourcecodeRepositoryPropertiesConfig.class,
        JGitSourcecodeRepositoryTest.JGitSourcecodeRepositoryConfig.class
})
@TestPropertySource("classpath:application-test.properties")
@ConfigurationPropertiesScan("com.wuxp.codegen")
class JGitSourcecodeRepositoryTest {

    @SpyBean
    private SourcecodeRepository sourcecodeRepository;

    @Test
    void testDownload() {
        when(sourcecodeRepository.download(anyString())).thenReturn("mock");
        String filepath = sourcecodeRepository.download("common-codegen");
        Assertions.assertEquals("mock", filepath);
    }

    @Test
    void testGetBranchList() {
        List<String> branchList = sourcecodeRepository.getBranchList("common-codegen");
        Assertions.assertFalse(branchList.isEmpty());
    }


    @Configuration
    public static class JGitSourcecodeRepositoryConfig {

        @Bean
        public SourcecodeRepository gitSourcecodeRepository(SourcecodeRepositoryPropertiesConfig config) {
            return new JGitSourcecodeRepository(config.getRepositories().get(0));
        }
    }


}