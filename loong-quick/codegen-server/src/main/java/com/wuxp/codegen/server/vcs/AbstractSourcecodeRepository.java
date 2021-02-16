package com.wuxp.codegen.server.vcs;

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

    protected AbstractSourcecodeRepository(SourcecodeRepositoryProperties properties) {
        super(properties);
        this.masterBranchName = properties.getMasterBranchName();
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

    private void deleteLocalRepository(File file) {
        if (log.isInfoEnabled()) {
            log.info("delete local repository,filepath={}", file.getAbsolutePath());
        }
        file.deleteOnExit();
    }

    /**
     * 获取远程仓库地址
     *
     * @param projectName 项目名称
     * @return 远程仓库地址
     */
    protected String getRemoteRepositoryDir(String projectName) {
        return String.format("%s/%s", this.getUri(), projectName);
    }

}
