
package com.wuxp.codegen.model.extensions;

import lombok.Data;

import java.io.Serializable;

/**
 * 类型变量
 *
 * @author wuxp
 */
@Data
public class SchemaCodegenModelTypeVariable implements Serializable {

    private static final long serialVersionUID = -2979682569545676057L;

    private String name;
}