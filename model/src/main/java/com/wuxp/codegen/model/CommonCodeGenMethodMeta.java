package com.wuxp.codegen.model;


import java.util.Map;

/**
 * 通用的代码生成method元数据
 */
public class CommonCodeGenMethodMeta extends CommonBaseMeta {

    /**
     * 返回值描述
     * 支持泛型的描述
     */
    private String returnType;

    /**
     * 参数列表
     */
    private Map<String/*参数名称*/,String/*参数类型描述*/> params;

    /**
     * 参数上定义的注解
     */
    private Map<String,CommonCodeGenAnnotation[]> paramAnnotations;

    /**
     * 注解
     */
    private CommonCodeGenAnnotation[]  annotations;

}
