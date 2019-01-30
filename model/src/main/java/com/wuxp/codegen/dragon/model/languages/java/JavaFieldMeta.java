package com.wuxp.codegen.dragon.model.languages.java;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;


/**
 * 属性元数据信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JavaFieldMeta extends JavaBaseMeta {


    // filed
    private Field field;

    //属性类型 如果有泛型则有多个
    private Class<?>[] types;

    //是否为 volatile 变量
    private Boolean isVolatile;

    //是否序列化
    private Boolean isTransient;

    //是否为枚举的常量
    private Boolean isEnumConstant;
}
