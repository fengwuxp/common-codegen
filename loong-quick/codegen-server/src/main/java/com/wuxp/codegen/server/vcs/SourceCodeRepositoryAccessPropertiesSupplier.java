package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import com.wuxp.codegen.server.entities.CodeVersionControlConfig;
import com.wuxp.codegen.server.mapstruct.LoongCodegenStructMapper;
import com.wuxp.codegen.server.repositories.CodeVersionControlConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 从数据库和配置文件中获获取{@link SourcecodeRepositoryProperties}
 *
 * @author wuxp
 */
@Slf4j
public class SourceCodeRepositoryAccessPropertiesSupplier implements Supplier<List<SourceCodeRepositoryAccessProperties>> {

    private static final String SCM_ACCESSOR_PROPERTIES_CACHE_NAME = "source_code_repository_configs";

    private final CodeVersionControlConfigRepository codeVersionControlConfigRepository;

    private final List<SourcecodeRepositoryProperties> sourcecodeRepositoryProperties;

    public SourceCodeRepositoryAccessPropertiesSupplier(CodeVersionControlConfigRepository codeVersionControlConfigRepository,
                                                        List<SourcecodeRepositoryProperties> sourcecodeRepositoryProperties) {
        this.codeVersionControlConfigRepository = codeVersionControlConfigRepository;
        this.sourcecodeRepositoryProperties = sourcecodeRepositoryProperties;
    }

    /**
     * 用于获取所有的 {@link SourcecodeRepositoryProperties} 配置
     *
     * @return {@link SourcecodeRepositoryProperties} 配置列表
     */
    @Override
    @Cacheable(value = SCM_ACCESSOR_PROPERTIES_CACHE_NAME)
    public List<SourceCodeRepositoryAccessProperties> get() {
        List<CodeVersionControlConfig> configs = codeVersionControlConfigRepository.findAll();
        List<SourcecodeRepositoryProperties> properties = LoongCodegenStructMapper.INSTANCE.mapSourcecodeRepositoryConfigs(configs);
        if (CollectionUtils.isEmpty(sourcecodeRepositoryProperties)) {
            properties.addAll(sourcecodeRepositoryProperties);
        }
        List<SourceCodeRepositoryAccessProperties> result = new ArrayList<>(sourcecodeRepositoryProperties);
        return Collections.unmodifiableList(result);
    }


}
