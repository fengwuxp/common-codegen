package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import lombok.extern.slf4j.Slf4j;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNAuthenticationManager;
import org.tmatesoft.svn.core.wc2.*;

import java.io.File;

/**
 * 从svn拉取代码
 *
 * @author wuxp
 */
@Slf4j
public class SvnKitSourcecodeRepository extends AbstractSourcecodeRepository {

    public SvnKitSourcecodeRepository(SourcecodeRepositoryProperties properties) {
        super(properties);
    }


    @Override
    public boolean exist(String projectName, String branch) {
        SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
        SvnList list = svnOperationFactory.createList();
        String remoteRepositoryUrl = this.getRemoteRepositoryUrl(projectName);
        try {
            list.setSingleTarget(SvnTarget.fromURL(SVNURL.parseURIEncoded(remoteRepositoryUrl)));
            return list.isFetchLocks();
        } catch (SVNException exception) {
            log.error("svn仓库{}不存在，项目名称：{}，分支：{}的代码，message：{}", getUri(), projectName, branch, exception.getMessage(), exception);
        }
        return false;
    }

    @Override
    protected void downloadByScm(String projectName, String branch, File workingDirectory) throws SVNException {
        String id = this.checkout(workingDirectory, projectName);
        if (log.isDebugEnabled()) {
            log.debug("svn checkout id：{}", id);
        }
    }

    @Override
    protected void updateProject(String projectName, String branch, File workingDirectory) throws SVNException {
        SvnOperationFactory svnOperationFactory = getSvnOperationFactory();
        SvnUpdate update = svnOperationFactory.createUpdate();
        update.setSingleTarget(SvnTarget.fromFile(workingDirectory));
        update.run();
    }


    private String checkout(File workDir, String projectName) throws SVNException {
        SvnOperationFactory svnOperationFactory = getSvnOperationFactory();
        String remoteRepositoryUrl = this.getRemoteRepositoryUrl(projectName);
        if (log.isDebugEnabled()) {
            log.debug("Checking out {} to: {}", remoteRepositoryUrl, workDir.getAbsolutePath());
        }
        final SvnCheckout checkout = svnOperationFactory.createCheckout();
        checkout.setSource(SvnTarget.fromURL(SVNURL.parseURIEncoded(remoteRepositoryUrl)));
        checkout.setSingleTarget(SvnTarget.fromFile(workDir));
        Long id = checkout.run();
        if (id == null) {
            return null;
        }
        return id.toString();
    }

    private SvnOperationFactory getSvnOperationFactory() {
        SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
        svnOperationFactory.setAuthenticationManager(new DefaultSVNAuthenticationManager(null,
                false, getUsername(), getPassword()));
        return svnOperationFactory;
    }

}
