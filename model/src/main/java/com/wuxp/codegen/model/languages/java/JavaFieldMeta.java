package com.wuxp.codegen.model.languages.java;


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


    /**
     * filed
     */
    private Field field;

    /**
     * 属性类型 如果有泛型则有多个
     */
    private Class<?>[] types;

    /**
     * 是否为 volatile 变量
     */
    private Boolean isVolatile;

    /**
     * 是否序列化
     */
    private Boolean isTransient;

    /**
     * 是否为枚举的常量
     */
    private Boolean isEnumConstant;

//    @Override
//    public int hashCode() {
//
//        return Objects.hashCode(this);
//    }
//
//    @Override
//    public boolean equals(final Object object) {
//        if (object == this) {
//            return true;
//        }
//        if (object == null) {
//            return false;
//        }
//
//        if (!(object instanceof JavaFieldMeta)) {
//            return false;
//        }
//
//        JavaFieldMeta javaFieldMeta = (JavaFieldMeta) object;
//
//        if (javaFieldMeta.getName() == null) {
//            return false;
//        }
//
//        if (this.getName() == null) {
//            return false;
//        }
//
//        return this.getName().endsWith(javaFieldMeta.getName());
//
//
//    }
}
