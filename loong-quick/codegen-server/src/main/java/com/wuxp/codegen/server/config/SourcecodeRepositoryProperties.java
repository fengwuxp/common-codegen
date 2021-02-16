package com.wuxp.codegen.server.config;

import com.wuxp.codegen.server.enums.SourcecodeRepositoryType;
import com.wuxp.codegen.server.vcs.support.AbstractScmAccessorProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 源代码仓储的配置
 *
 * @author wuxp
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SourcecodeRepositoryProperties extends AbstractScmAccessorProperties {


    /**
     * 仓库唯一标识
     * 没有设置或只有一个仓库则使用默认值
     */
    private String code = "default";

    /**
     * 仓库类型
     */
    private SourcecodeRepositoryType type = SourcecodeRepositoryType.GIT;

    /**
     * 主分支名称
     */
    private String masterBranchName = SourcecodeRepositoryType.GIT.getMainBranchName();

}
