package com.wuxp.codegen.model.languages.java;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Type;


/**
 * 属性元数据信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class JavaFieldMeta extends JavaBaseMeta {


    //属性类型 如果有泛型则有多个
    private Type[] types;


    private Boolean isVolatile;

    //是否序列化
    private Boolean isTransient;
}
