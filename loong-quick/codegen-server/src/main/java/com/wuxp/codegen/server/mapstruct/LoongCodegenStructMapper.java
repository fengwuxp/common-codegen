package com.wuxp.codegen.server.mapstruct;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import com.wuxp.codegen.server.entities.ScmInfo;
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
     * 转换 {@link  ScmInfo} to {@link SourcecodeRepositoryProperties}
     *
     * @param scmInfos
     * @return
     */
    List<SourcecodeRepositoryProperties> mapScmInfoToSourcecodeRepositoryProperties(List<ScmInfo> scmInfos);
}
