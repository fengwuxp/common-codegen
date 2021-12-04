package com.wuxp.codegen.server.config;

import com.wuxp.codegen.server.enums.SourcecodeRepositoryType;
import com.wuxp.codegen.server.vcs.SourceCodeRepositoryAccessProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.core.Ordered;

import static com.wuxp.codegen.server.constant.VcsConstants.DEFAULT_REPOSITORY_NANE;

/**
 * 源代码仓储的配置
 *
 * @author wuxp
 */
@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
public class SourcecodeRepositoryProperties implements SourceCodeRepositoryAccessProperties {

    private String name = DEFAULT_REPOSITORY_NANE;

    private SourcecodeRepositoryType type = SourcecodeRepositoryType.GIT;

    private String uri;

    private String basedir;

    private String masterBranchName = type.getMainBranchName();

    private String username;

    private String password;

    private String passphrase;

    private boolean strictHostKeyChecking = true;

    private int order = Ordered.LOWEST_PRECEDENCE;


}
