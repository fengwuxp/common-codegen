package com.wuxp.codegen.server.constant;


/**
 * 代码版本管理相关常量
 */
public final class VcsConstants {

    private VcsConstants() {
        throw new AssertionError();
    }

    /**
     * 默认的仓库 name
     */
    public static final String DEFAULT_REPOSITORY_NANE = "default";

    public static final String DEFAULT_GIT_BRANCH_NAME = "master";

    public static final String DEFAULT_SVN_BRANCH_NAME = "main";

    /**
     * 默认生成的模块名称
     */
    public static final String DEFAULT_MODULE_NAME = "web";
}
