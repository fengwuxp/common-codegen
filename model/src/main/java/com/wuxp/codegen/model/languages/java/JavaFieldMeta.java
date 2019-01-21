package com.wuxp.codegen.model.languages.java;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 属性元数据信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JavaFieldMeta extends JavaBaseMeta {


    //属性类型 如果有泛型则有多个
    private Class<?>[] types;


    private Boolean isVolatile;

    //是否序列化
    private Boolean isTransient;
}
