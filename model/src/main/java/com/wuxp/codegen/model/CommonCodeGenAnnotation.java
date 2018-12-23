package com.wuxp.codegen.model;


import lombok.Data;

import java.util.Map;

/**
 * 通用的代码生成注解描述元数据
 */
@Data
public class CommonCodeGenAnnotation extends CommonBaseMeta{

    /**
     * 注解的配置or参数
     * 如果是对象参数 key为参数的名称
     * 如果是参数列表，key为参数的位置
     */
    private Map<String,String> options;
}
