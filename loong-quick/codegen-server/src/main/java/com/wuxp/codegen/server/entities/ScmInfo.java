package com.wuxp.codegen.server.entities;

import com.wuxp.codegen.server.enums.SourcecodeRepositoryType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * source code manager system 信息表，例如git、svn
 *
 * @author wuxp
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "t_scm_info", uniqueConstraints ={@UniqueConstraint(name = "uk_code",columnNames = "code")})
@Entity
public class ScmInfo extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -2132220160441070579L;

    /**
     * code必须唯一
     */
    @Column(length = 32, nullable = false)
    private String code;

    @Column(length = 8, nullable = false)
    @Enumerated(EnumType.STRING)
    private SourcecodeRepositoryType type;

    /**
     * URI of remote repository.
     */
    private String uri;

    /**
     * Base directory for local working copy of repository.
     */
    private String basedir;

}
