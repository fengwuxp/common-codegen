package com.wuxp.codegen.model;


import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 通用的代码生成注解描述元数据
 */
@Data
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
