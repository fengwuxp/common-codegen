package com.wuxp.codegen.model;


import lombok.Data;

import java.util.Map;

/**
 * 通用的代码生成method元数据
 */
@Data
public class CommonCodeGenMethodMeta extends CommonBaseMeta {

    /**
     * 返回值类型
     * 大于一个表示有泛型泛型
     */
    private CommonCodeGenClassMeta[] returnTypes;

    /**
     * 参数列表
     */
    private Map<String/*参数名称*/,CommonCodeGenClassMeta/*参数类型描述*/> params;

    /**
     * 参数上定义的注解
     */
    private Map<String,CommonCodeGenAnnotation[]> paramAnnotations;

    /**
     * 注解
     */
    private CommonCodeGenAnnotation[]  annotations;

    /**
     * 类型参数, 泛型
     */
    protected CommonCodeGenClassMeta[] typeVariables;

}
