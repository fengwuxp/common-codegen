package com.wuxp.codegen.model;


import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 通用的代码生成method元数据
 *
 * @author wuxp
 */
@EqualsAndHashCode(exclude = {"declaringClassMeta", "returnTypes", "params", "typeVariables", "paramAnnotations"}, callSuper = true)
@Data
@Accessors(chain = true)
public class CommonCodeGenMethodMeta extends CommonBaseMeta {

    /**
     * 原目标 Method
     */
    private Method source;

    private JavaMethodMeta javaMethodMeta;

    private JavaClassMeta declaringClassMeta;

    /**
     * 类型参数, 泛型
     */
    protected CommonCodeGenClassMeta[] typeVariables;
    /**
     * 返回值类型 大于一个表示有泛型泛型
     */
    private CommonCodeGenClassMeta[] returnTypes;
    /**
     * 参数列表
     *
     * @key 参数名称
     * @value 参数类型描述
     */
    private Map<String, CommonCodeGenClassMeta> params;
    /**
     * 参数上定义的注解
     */
    private Map<String, CommonCodeGenAnnotation[]> paramAnnotations;
    /**
     * 注解
     */
    private CommonCodeGenAnnotation[] annotations;

}
