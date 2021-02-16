package com.wuxp.codegen.server.enums;

import lombok.Getter;

/**
 * @author wuxp
 */
@Getter
public enum SourcecodeRepositoryType {

    /**
     * git
     */
    GIT("master"),

    /**
     * svn
     */
    SVN("");

    private final String mainBranchName;

    SourcecodeRepositoryType(String mainBranchName) {
        this.mainBranchName = mainBranchName;
    }
}
