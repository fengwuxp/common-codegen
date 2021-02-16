package com.wuxp.codegen.server.vcs;

import com.wuxp.codegen.server.config.SourcecodeRepositoryProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

/**
 * 从git拉取代码
 *
 * @author wuxp
 */
@Slf4j
public class JGitSourcecodeRepository extends AbstractSourcecodeRepository {

    private final JGitFactory gitFactory;

    public JGitSourcecodeRepository( SourcecodeRepositoryProperties properties) {
        super(properties);
        gitFactory = new JGitFactory(false);
    }

    @Override
    public String download(String projectName, String branch) {
        try {
            CloneCommand cloneCommand = gitFactory.getCloneCommandByCloneRepository();
            File workingDirectory = this.getWorkingDirectory(projectName, branch);
            cloneCommand.setURI(this.getRemoteRepositoryDir(projectName))
                    .setBranch(branch)
                    .setDirectory(workingDirectory)
                    .setCredentialsProvider(credentialsProvider())
                    .call();
            return workingDirectory.getAbsolutePath();
        } catch (GitAPIException exception) {
            log.error("从git仓库拉取代码失败：{}", exception.getMessage(), exception);
            throw new VcsException(exception);
        }
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
}
