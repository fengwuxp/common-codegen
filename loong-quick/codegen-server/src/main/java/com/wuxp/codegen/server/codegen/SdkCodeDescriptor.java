package com.wuxp.codegen.server.codegen;

import com.wuxp.codegen.core.ClientProviderType;


/**
 * 描述一份 sdk code，用于指向 code 所在的位置
 */
public interface SdkCodeDescriptor {

    /**
     * 项目名称
     */
    String getProjectName();

    /**
     * sdk 的 client provider type
     */
    String getBranch();


    /**
     * 代码的模块/目录，用于项目中存在多个模块可以生成 sdk 时使用
     * 默认值：web
     */
    String getModuleName();

    /**
     * 代码分支
     * 默认值： master
     *
     * @see com.wuxp.codegen.server.vcs.SourcecodeRepository#getMasterBranchName()
     */
    ClientProviderType getType();


}
