package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * svn
 *
 * @author wuxp
 */
@Slf4j
public class SvnKitSourcecodeRepository extends AbstractSourcecodeRepository {

    public SvnKitSourcecodeRepository(SourcecodeRepositoryProperties properties) {
        super(properties);
    }

    @Override
    public String download(String projectName, String branch) {
        // TODO
        return null;
    }
}
