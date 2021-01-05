package com.wuxp.codegen.model.languages.java;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;


/**
 * 参数元数据信息
 *
 * @author wuxp
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class JavaParameterMeta extends JavaFieldMeta {

    /**
     * parameter
     */
    private Parameter parameter;




}
