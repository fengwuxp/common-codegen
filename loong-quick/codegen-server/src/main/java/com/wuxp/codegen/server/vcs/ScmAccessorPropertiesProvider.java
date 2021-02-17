package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import com.wuxp.codegen.server.entities.ScmInfo;
import com.wuxp.codegen.server.repositories.ScmInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author wuxp
 */
@Slf4j
@Component
public class ScmAccessorPropertiesProvider {

    private final ScmInfoRepository scmInfoRepository;

    public ScmAccessorPropertiesProvider(ScmInfoRepository scmInfoRepository) {
        this.scmInfoRepository = scmInfoRepository;
    }

    public List<SourcecodeRepositoryProperties> getRepositoryProperties() {

        List<ScmInfo> scmInfos = scmInfoRepository.findAll();

        return Collections.emptyList();
    }
}
