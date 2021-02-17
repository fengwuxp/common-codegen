package com.wuxp.codegen.server.entities;

import com.wuxp.codegen.server.enums.SourcecodeRepositoryType;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * source code manager system 信息表，例如git\svn
 *
 * @author wuxp
 */
@Data
@Table(name = "t_scm_info")
@Entity
public class ScmInfo implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 32,nullable = false)
    private String code;

    @Column(length = 8,nullable = false)
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

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @CreatedBy
    private String creator;

    @LastModifiedBy
    private String modifier;
}
