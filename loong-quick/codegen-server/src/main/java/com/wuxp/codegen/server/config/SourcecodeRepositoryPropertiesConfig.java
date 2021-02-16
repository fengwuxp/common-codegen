package com.wuxp.codegen.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 源代码仓储的配置
 *
 * @author wuxp
 */
@Configuration
@ConfigurationProperties(prefix = SourcecodeRepositoryConfig.REPOSITORY_PREFIX)
@Getter
@Setter
public class SourcecodeRepositoryPropertiesConfig {

    private List<SourcecodeRepositoryProperties> repositories;
}
