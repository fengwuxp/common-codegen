package com.wuxp.codegen.server.mapstruct;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import com.wuxp.codegen.server.entities.CodeVersionControlConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author wuxp
 */
@Mapper
public interface LoongCodegenStructMapper {

    LoongCodegenStructMapper INSTANCE = Mappers.getMapper(LoongCodegenStructMapper.class);

    /**
     * 转换 {@link  CodeVersionControlConfig} to {@link SourcecodeRepositoryProperties}
     */
    SourcecodeRepositoryProperties mapSourcecodeRepositoryConfig(CodeVersionControlConfig config);

    /**
     * 转换 {@link  CodeVersionControlConfig} to {@link SourcecodeRepositoryProperties}
     */
    List<SourcecodeRepositoryProperties> mapSourcecodeRepositoryConfigs(List<CodeVersionControlConfig> configs);
}
