package com.wuxp.codegen.model.languages.java;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;


/**
 * 属性元数据信息
 *
 * @author wuxp
 */
@Getter
@Setter
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
    private Boolean isEnumConstant = false;

    public JavaFieldMeta() {
        super();
    }

    public JavaFieldMeta(int modifiers) {
        super(modifiers);
        this.setIsTransient(Modifier.isTransient(modifiers))
                .setIsVolatile(Modifier.isVolatile(modifiers));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        JavaFieldMeta that = (JavaFieldMeta) o;
        return Objects.equals(field, that.field) &&
                Arrays.equals(types, that.types) &&
                Objects.equals(isVolatile, that.isVolatile) &&
                Objects.equals(isTransient, that.isTransient) &&
                Objects.equals(isEnumConstant, that.isEnumConstant);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), field, isVolatile, isTransient, isEnumConstant);
        result = 31 * result + Arrays.hashCode(types);
        return result;
    }
}
