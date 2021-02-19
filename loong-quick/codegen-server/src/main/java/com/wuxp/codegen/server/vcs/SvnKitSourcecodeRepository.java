package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import lombok.extern.slf4j.Slf4j;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc2.SvnCheckout;
import org.tmatesoft.svn.core.wc2.SvnList;
import org.tmatesoft.svn.core.wc2.SvnOperationFactory;
import org.tmatesoft.svn.core.wc2.SvnTarget;

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
    public String download(String projectName, String branch) {
        SvnOperationFactory svnOperationFactory = new SvnOperationFactory();
        File workingDirectory = this.getWorkingDirectory(projectName, branch);
        try {
            String id = this.checkout(svnOperationFactory, workingDirectory, projectName);
            if (log.isDebugEnabled()) {
                log.debug("svn checkout id：{}", id);
            }
        } catch (SVNException exception) {
            log.error("从svn仓库拉取代码失败，项目名称：{}，分支：{}，message：{}", projectName, branch, exception.getMessage(), exception);
            throw new VcsException(exception);
        }
        return workingDirectory.getAbsolutePath();
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

    private String checkout(SvnOperationFactory svnOperationFactory, File workDir, String projectName) throws SVNException {
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
}
