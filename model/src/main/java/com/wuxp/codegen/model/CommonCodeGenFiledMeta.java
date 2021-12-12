package com.wuxp.codegen.model;


import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;


/**
 * 通用的代码生成 filed 元数据
 *
 * @author wuxp
 */
@EqualsAndHashCode(exclude = {"declaringClassMeta", "filedTypes", "annotations", "typeVariables"}, callSuper = true)
@Data
@Accessors(chain = true)
public class CommonCodeGenFiledMeta extends CommonBaseMeta {

    /**
     * 原目标 Field
     */
    private Field source;

    private JavaFieldMeta fieldMeta;

    private JavaClassMeta declaringClassMeta;

    /**
     * 域对象类型列表 大于一个表示有泛型泛型
     */
    protected CommonCodeGenClassMeta[] filedTypes;

    /**
     * 注解
     */
    protected CommonCodeGenAnnotation[] annotations;

    /**
     * 类型参数, 泛型
     */
    protected CommonCodeGenClassMeta[] typeVariables;

    /**
     * 是否为枚举常量
     */
    private boolean enumConstant = false;

    /**
     * 枚举的内部字段值
     */
    private String[] enumFiledValues;

    /**
     * 是否必填
     */
    private Boolean required = false;
}
