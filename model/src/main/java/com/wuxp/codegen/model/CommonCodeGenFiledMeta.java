package com.wuxp.codegen.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 通用的代码生成 filed 元数据
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CommonCodeGenFiledMeta extends CommonBaseMeta {


    /**
     * 域对象类型列表
     * 大于一个表示有泛型泛型
     */
    private CommonCodeGenClassMeta[] filedTypes;

    /**
     * 注解
     */
    private CommonCodeGenAnnotation[] annotations;

    /**
     * 类型参数, 泛型
     */
    protected CommonCodeGenClassMeta[] typeVariables;
}
