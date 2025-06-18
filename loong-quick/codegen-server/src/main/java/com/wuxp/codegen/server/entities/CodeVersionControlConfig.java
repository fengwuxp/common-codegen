package com.wuxp.codegen.server.entities;

import com.wuxp.codegen.server.enums.SourcecodeRepositoryType;
import com.wuxp.codegen.server.vcs.SourceCodeRepositoryAccessProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * 代码管理仓库信息 例如 git、svn
 *
 * @author wuxp
 */
@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "t_code_vcs_config", uniqueConstraints = {@UniqueConstraint(name = "uk_name", columnNames = "name")})
@Entity
public class CodeVersionControlConfig extends AbstractEntity implements SourceCodeRepositoryAccessProperties, Serializable {

    private static final long serialVersionUID = -2132220160441070579L;

    /**
     * 代码仓库标识 name 必须唯一
     */
    @Column(length = 32, nullable = false)
    private String name;

    /**
     * 代码仓库类型
     */
    @Column(length = 8, nullable = false)
    @Enumerated(EnumType.STRING)
    private SourcecodeRepositoryType type;

    /**
     * URI of remote repository.
     * 代码仓库的跟地址
     */
    @Column(nullable = false)
    private String uri;

    /**
     * Base directory for local working copy of repository.
     */
    @Column(nullable = false)
    private String basedir;

    /**
     * 主分支名称
     */
    @Column(length = 32, nullable = true)
    private String masterBranchName;

    /**
     * 用于登录的用户名
     */
    @Column(length = 32, nullable = true)
    private String username;

    /**
     * 用于登录的密码
     */
    @Column(length = 32, nullable = true)
    private String password;

    /**
     * ssh key
     */
    @Column(nullable = true)
    private String passphrase;

    @Column(length = 1, nullable = false)
    private Boolean strictHostKeyChecking = true;

    @Override
    public boolean isStrictHostKeyChecking() {
        return Boolean.TRUE.equals(strictHostKeyChecking);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, type, uri, basedir);
    }
}
