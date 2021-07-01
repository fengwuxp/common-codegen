package com.wuxp.codegen.model.languages.java;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Parameter;


/**
 * 参数元数据信息
 *
 * @author wuxp
 */
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class JavaParameterMeta extends JavaFieldMeta {

    /**
     * parameter
     */
    private Parameter parameter;

    public JavaParameterMeta() {
        super();
    }
}
