package com.wuxp.codegen.model;

import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author wuxp
 * @date 2024-08-13 16:48
 **/
@Data
public class CommonCodeGenEnumFiledValue {

    private Field field;

    private String enumValue;

    public CommonCodeGenEnumFiledValue(Field field, String enumValue) {
        this.field = field;
        this.enumValue = enumValue;
    }
}
