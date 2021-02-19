package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * 从git拉取代码
 *
 * @author wuxp
 */
@Slf4j
public class JGitSourcecodeRepository extends AbstractSourcecodeRepository {

    private final JGitFactory gitFactory;

    public JGitSourcecodeRepository(SourcecodeRepositoryProperties properties) {
        super(properties);
        gitFactory = new JGitFactory(false);
    }

    @Override
    public String download(String projectName, String branch) {
        File workingDirectory = this.getWorkingDirectory(projectName, branch);
        String remoteRepositoryUrl = this.getRemoteRepositoryUrl(projectName);
        if (log.isDebugEnabled()) {
            log.debug("Checking out {} to: {}", remoteRepositoryUrl, workingDirectory.getAbsolutePath());
        }
        try {
            CloneCommand cloneCommand = gitFactory.getCloneCommandByCloneRepository();
            cloneCommand.setURI(remoteRepositoryUrl)
                    .setBranch(branch)
                    .setDirectory(workingDirectory)
                    .setCredentialsProvider(credentialsProvider())
                    .call();
            return workingDirectory.getAbsolutePath();
        } catch (GitAPIException exception) {
            log.error("从git仓库{}拉取代码失败，项目名称：{}，分支：{}，message：{}", getUri(), projectName, branch, exception.getMessage(), exception);
            throw new VcsException(exception);
        }
    }

    @Override
    public boolean exist(String projectName, String branch) {
        String remoteRepositoryUrl = this.getRemoteRepositoryUrl(projectName);
        try {
            Collection<Ref> refCollection = Git.lsRemoteRepository().setRemote(remoteRepositoryUrl).call();
            return !refCollection.isEmpty();
        } catch (GitAPIException exception) {
            log.error("git仓库{}不存在，项目名称：{}，分支：{}的代码，message：{}", getUri(), projectName, branch, exception.getMessage(), exception);
        }
        return false;
    }

    private CredentialsProvider credentialsProvider() {
        return new UsernamePasswordCredentialsProvider(getUsername(), getPassword());
    }

    private Git openGitRepository(String projectName, String branch) throws IOException {
        return this.gitFactory.getGitByOpen(getWorkingDirectory(projectName, branch));
    }

    /**
     * copy from Spring Cloud Config
     * Wraps the static method calls to {@link org.eclipse.jgit.api.Git} and
     * {@link org.eclipse.jgit.api.CloneCommand} allowing for easier unit testing.
     */
    public static class JGitFactory {

        private final boolean cloneSubmodules;

        public JGitFactory() {
            this(false);
        }

        public JGitFactory(boolean cloneSubmodules) {
            this.cloneSubmodules = cloneSubmodules;
        }

        public Git getGitByOpen(File file) throws IOException {
            return Git.open(file);
        }

        public CloneCommand getCloneCommandByCloneRepository() {
            return Git.cloneRepository()
                    .setCloneSubmodules(cloneSubmodules);
        }
    }

    @Override
    protected String getRemoteRepositoryUrl(String projectName) {
        String remoteRepositoryUrl = super.getRemoteRepositoryUrl(projectName);
        if (remoteRepositoryUrl.startsWith("http")) {
            return remoteRepositoryUrl;
        }
        // ssh url
        return remoteRepositoryUrl + ".git";
    }
}
