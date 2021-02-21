package com.wuxp.codegen.server.vcs;

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
     * 使用默认分支，下载项目
     * @param projectName 项目名称
     * @return 下载项目的本地目录
     */
    default String download(String projectName) {
        return download(projectName, this.getMasterBranchName());
    }

    /**
     * 下载项目
     *
     * @param projectName 项目名称
     * @param branch      分支名称
     * @return 下载项目的本地目录
     */
    String download(String projectName, String branch);

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
        return "master";
    }
}
