package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import com.wuxp.codegen.server.config.SourcecodeRepositoryPropertiesConfig;
import com.wuxp.codegen.server.entities.ScmInfo;
import com.wuxp.codegen.server.mapstruct.LoongCodegenStructMapper;
import com.wuxp.codegen.server.repositories.ScmInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * 从数据库和配置文件中获获取{@link SourcecodeRepositoryProperties}
 *
 * @author wuxp
 */
@Slf4j
//@Component
public class ScmAccessorPropertiesProvider {

    private static final String SCM_ACCESSOR_PROPERTIES_CACHE_NAME = "repository_info_cache";

    private final ScmInfoRepository scmInfoRepository;

    private final SourcecodeRepositoryPropertiesConfig repositoryPropertiesConfig;


    public ScmAccessorPropertiesProvider(ScmInfoRepository scmInfoRepository, SourcecodeRepositoryPropertiesConfig repositoryPropertiesConfig) {
        this.scmInfoRepository = scmInfoRepository;
        this.repositoryPropertiesConfig = repositoryPropertiesConfig;
    }

    /**
     * 用于获取所有的 {@link SourcecodeRepositoryProperties} 配置
     *
     * @return {@link SourcecodeRepositoryProperties} 配置列表
     */
    @Cacheable(value = SCM_ACCESSOR_PROPERTIES_CACHE_NAME, key = "all")
    public List<SourcecodeRepositoryProperties> getRepositoryProperties() {
        List<ScmInfo> scmInfos = scmInfoRepository.findAll();
        List<SourcecodeRepositoryProperties> sourcecodeRepositoryProperties = LoongCodegenStructMapper.INSTANCE.mapScmInfoToSourcecodeRepositoryProperties(scmInfos);
        List<SourcecodeRepositoryProperties> repositories = repositoryPropertiesConfig.getRepositories();
        if (repositories != null) {
            sourcecodeRepositoryProperties.addAll(repositories);
        }
        return sourcecodeRepositoryProperties;
    }

}
