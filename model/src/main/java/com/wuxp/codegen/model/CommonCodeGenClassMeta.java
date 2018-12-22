package com.wuxp.codegen.model;

import java.util.Map;

/**
 * 通用的代码生成 class 元数据
 */
public class CommonCodeGenClassMeta extends CommonBaseMeta {

    /**
     * 注解
     */
    private CommonCodeGenAnnotation[]  annotations;

    /**
     * 包所在路径
     */
    private String packagePath;

    /**
     * 依赖
     */
    private Map<String,String> dependencies;
}
