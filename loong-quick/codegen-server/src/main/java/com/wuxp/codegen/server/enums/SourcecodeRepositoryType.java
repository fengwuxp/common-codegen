package com.wuxp.codegen.server.enums;

import lombok.Getter;

import static com.wuxp.codegen.server.constant.VcsConstants.DEFAULT_GIT_BRANCH_NAME;
import static com.wuxp.codegen.server.constant.VcsConstants.DEFAULT_SVN_BRANCH_NAME;

/**
 * @author wuxp
 */
@Getter
public enum SourcecodeRepositoryType {

    /**
     * git
     */
    GIT(DEFAULT_GIT_BRANCH_NAME),

    /**
     * svn
     */
    SVN(DEFAULT_SVN_BRANCH_NAME);

    private final String mainBranchName;

    SourcecodeRepositoryType(String mainBranchName) {
        this.mainBranchName = mainBranchName;
    }
}
