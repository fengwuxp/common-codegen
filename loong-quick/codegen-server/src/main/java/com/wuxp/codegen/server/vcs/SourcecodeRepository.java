package com.wuxp.codegen.server.vcs;

import java.util.List;

import static com.wuxp.codegen.server.constant.VcsConstants.DEFAULT_GIT_BRANCH_NAME;

/**
 * 源代码仓库
 * 用于从源代码管理平台拉取代码
 * <p>
 * 按照{project}/{branch}进行目录分隔
 * </p>
 *
 * @author wuxp
 */
public interface SourcecodeRepository {


    /**
     * 使用默认分支，checkout
     *
     * @param projectName 项目名称
     * @return checkout 项目的本地目录
     */
    default String checkout(String projectName) {
        return checkout(projectName, this.getMasterBranchName());
    }

    /**
     * checkout
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     * @return checkout 项目的本地目录
     */
    String checkout(String projectName, String branch);

    /**
     * 获取项目的分支列表
     *
     * @param projectName 项目名称
     * @return 分支列表
     */
    List<String> getBranchList(String projectName);

    /**
     * 判断当前项目是否存在
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     * @return return <code>true</code> 表示存在
     */
    boolean exist(String projectName, String branch);

    /**
     * 删除本地的已经下载的源代码路径
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     */
    void deleteLocalRepository(String projectName, String branch);

    /**
     * 删除本地的已经下载的源代码路径
     *
     * @param filepath 本地源代码路径
     */
    void deleteLocalRepository(String filepath);

    /**
     * @return 默认分支的名称
     */
    default String getMasterBranchName() {
        return DEFAULT_GIT_BRANCH_NAME;
    }

    /**
     * 获取本地的工作路径
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     * @return 本地仓库的工作路径
     */
    String getLocalDirectory(String projectName, String branch);
}
