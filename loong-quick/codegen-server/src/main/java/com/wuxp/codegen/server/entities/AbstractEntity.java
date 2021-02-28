package com.wuxp.codegen.server.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wuxp
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = -6694969657556529713L;

    @Id
    @GeneratedValue
    protected Long id;

    @CreatedDate
    @Column(name = "create_time", nullable = false)
    protected LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "last_update_time", nullable = false)
    protected LocalDateTime lastUpdateTime;

    @CreatedBy
    @Column(name = "creator", nullable = false)
    protected String creator;

    @LastModifiedBy
    @Column(name = "modifier", nullable = false)
    protected String modifier;
}
