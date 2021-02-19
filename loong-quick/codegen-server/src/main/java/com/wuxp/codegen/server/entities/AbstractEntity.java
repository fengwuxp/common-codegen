package com.wuxp.codegen.server.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wuxp
 */
@Data
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = -6694969657556529713L;

    @Id
    @GeneratedValue
    protected Long id;

    @CreatedDate
    @Column(name = "create_time",nullable = false)
    protected LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "last_update_time",nullable = false)
    protected LocalDateTime lastUpdateTime;

    @CreatedBy
    @Column(name = "creator",nullable = false)
    protected String creator;

    @LastModifiedBy
    @Column(name = "modifier",nullable = false)
    protected String modifier;
}
