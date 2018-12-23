package com.wuxp.codegen.model;

import com.wuxp.codegen.model.enums.ClassType;
import lombok.Data;

import java.util.Map;

/**
 * 通用的代码生成 class 元数据
 */
@Data
public class CommonCodeGenClassMeta extends CommonBaseMeta {


    /**
     * 类类型
     */
    protected ClassType classType;

    /**
     * 是否为抽象的
     */
    protected Boolean isAbstract;

    /**
     * 超类
     */
    protected CommonCodeGenClassMeta superClass;

    /**
     * 实现的接口
     */
    protected CommonCodeGenClassMeta[] interfaces;

    /**
     * 注解
     */
    protected CommonCodeGenAnnotation[] annotations;

    /**
     * 包所在路径
     */
    protected String packagePath;

    /**
     * 依赖
     */
    protected Map<String, String> dependencies;

    /**
     * 在有泛型时候的描述
     * 例如 Set<T>,Map<K,V> 等
     */
    protected String genericDescription;


    /**
     * 方法列表
     */
    protected CommonCodeGenMethodMeta[] methodMetas;


    /**
     * 属性列表
     */
    protected CommonCodeGenFiledMeta[] filedMetas;
}
