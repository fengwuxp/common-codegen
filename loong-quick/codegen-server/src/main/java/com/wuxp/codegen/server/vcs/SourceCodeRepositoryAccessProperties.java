package com.wuxp.codegen.server.vcs;


import com.wuxp.codegen.server.enums.SourcecodeRepositoryType;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;


/**
 * 访问源代码仓库访问属性
 */
public interface SourceCodeRepositoryAccessProperties extends Ordered {

    /**
     * 仓库名称、唯一标识
     *
     * @return
     */
    String getName();

    /**
     * URI of remote repository.
     */
    String getUri();

    /**
     * Base directory for local working copy of repository.
     */
    String getBasedir();

    /**
     * Username for authentication with remote repository.
     */
    @Nullable
    String getUsername();

    /**
     * Password for authentication with remote repository.
     */
    @Nullable
    String getPassword();

    /**
     * Passphrase for unlocking your ssh private key.
     */
    @Nullable
    String getPassphrase();

    /**
     * Reject incoming SSH host keys from remote servers not in the known host list.
     */
    boolean isStrictHostKeyChecking();

    SourcecodeRepositoryType getType();

    default String getMasterBranchName() {
        return getType().getMainBranchName();
    }

    @Override
    default int getOrder() {
        return 0;
    }
}
