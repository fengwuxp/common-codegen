package com.wuxp.codegen.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * 通用的代码生成注解描述元数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CommonCodeGenAnnotation extends CommonBaseMeta{


    /**
     * 命名参数
     */
    private Map<String,String> namedArguments;

    /**
     * 位置参数
     */
    private List<String> positionArguments;
}
