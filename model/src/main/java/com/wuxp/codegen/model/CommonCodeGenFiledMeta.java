package com.wuxp.codegen.model;


import lombok.Data;

/**
 * 通用的代码生成 filed 元数据
 */
@Data
public class CommonCodeGenFiledMeta extends CommonBaseMeta {


    /**
     * 域对象类型描述
     * 支持泛型的描述
     */
    private String filedType;

    /**
     * 注解
     */
    private CommonCodeGenAnnotation[] annotations;

}
