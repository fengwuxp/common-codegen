package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.core.util.CodegenFileUtils;
import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import com.wuxp.codegen.server.vcs.support.AbstractScmAccessor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author wuxp
 */
@Slf4j
public abstract class AbstractSourcecodeRepository extends AbstractScmAccessor implements SourcecodeRepository {

    protected final String masterBranchName;

    protected AbstractSourcecodeRepository(SourceCodeRepositoryAccessProperties properties) {
        super(properties);
        this.masterBranchName = properties.getMasterBranchName();
    }

    @Override
    public String checkout(String projectName, String branch) {
        File workingDirectory = this.getWorkingDirectory(projectName, branch);
        synchronized (this) {
            if (workingDirectory.exists()) {
                // TODO 做更新？
                CodegenFileUtils.deleteDirectory(workingDirectory.getAbsolutePath());
            }
            boolean mkdir = workingDirectory.mkdir();
            if (mkdir) {
                log.info("创建本地仓库目录：{}", workingDirectory.getAbsolutePath());
            }
        }

        try {
            this.clone(projectName, branch, workingDirectory);
        } catch (Exception exception) {
            workingDirectory.deleteOnExit();
            log.error("从源代码平台{}拉取代码失败，项目名称：{}，分支：{}，message：{}", getUri(), projectName, branch, exception.getMessage(), exception);
            throw new VcsException(exception);
        }
        return workingDirectory.getAbsolutePath();
    }

    @Override
    public void deleteLocalRepository(String projectName, String branch) {
        deleteLocalRepository(getWorkingDirectory(projectName, branch));
    }

    @Override
    public void deleteLocalRepository(String filepath) {
        File file = new File(filepath);
        deleteLocalRepository(file);
    }

    @Override
    public String getMasterBranchName() {
        return this.masterBranchName;
    }

    @Override
    public String getLocalDirectory(String projectName, String branch) {
        return this.getWorkingDirectory(projectName, branch).getAbsolutePath();
    }

    /**
     * 从源代码平台下载代码
     *
     * @param projectName      项目名称
     * @param branch           分支名称
     * @param workingDirectory 本地仓库目录
     * @throws Exception
     */
    protected abstract void clone(String projectName, String branch, File workingDirectory) throws Exception;

    /**
     * 更新项目
     *
     * @param projectName      项目名称
     * @param branch           分支名称
     * @param workingDirectory 本地仓库目录
     */
    protected abstract void updateProject(String projectName, String branch, File workingDirectory) throws Exception;

    /**
     * 获取远程仓库地址
     *
     * @param projectName 项目名称
     * @return 远程仓库地址
     */
    protected String getRemoteRepositoryUrl(String projectName) {
        return String.format("%s/%s", this.getUri(), projectName);
    }

    private void deleteLocalRepository(File file) {
        if (log.isInfoEnabled()) {
            log.info("delete local repository,filepath={}", file.getAbsolutePath());
        }
        file.deleteOnExit();
    }


}
