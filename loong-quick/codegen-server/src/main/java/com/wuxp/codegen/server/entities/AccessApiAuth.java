package com.wuxp.codegen.server.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 访问 api 认证
 * @author wuxp
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "t_scm_info")
@Entity
public class AccessApiAuth extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 3317259478477210337L;

}
