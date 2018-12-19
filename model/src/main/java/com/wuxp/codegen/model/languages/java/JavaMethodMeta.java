package com.wuxp.codegen.model.languages.java;


import lombok.Builder;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 方法元数据信息
 */
@Data
@Builder
public class JavaMethodMeta extends JavaBaseMeta {


    //返回值类型，如果有泛型则有多个 void是为null
    private Class<?>[] returnType;

    //参数列表 返回的是LinkHashMap
    private Map<String/*参数名称*/, Class<?>[]> params;

    //参数上的注解
    private Map<String/*参数名称*/, Annotation[]> paramAnnotations;

    //是否为抽象方法
    private Boolean isAbstract;

    //是否为同步方法
    private Boolean isSynchronized;

    //是否为本地方法
    private Boolean isNative;

    //方法归属的类
    private Class<?> owner;
}
