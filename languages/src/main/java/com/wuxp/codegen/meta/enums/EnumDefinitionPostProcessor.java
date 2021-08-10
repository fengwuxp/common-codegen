package com.wuxp.codegen.meta.enums;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.meta.util.EnumUtils;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
public class EnumDefinitionPostProcessor implements LanguageDefinitionPostProcessor<CommonBaseMeta> {

    @Override
    public void postProcess(CommonBaseMeta meta) {
        Class<?> clazz = meta.getClass();
        if (clazz.isAssignableFrom(CommonCodeGenClassMeta.class)) {
            postProcessClassMeta((CommonCodeGenClassMeta) meta);
        }
        if (clazz.isAssignableFrom(CommonCodeGenFiledMeta.class)) {
            postProcessFiledMeta((CommonCodeGenFiledMeta) meta);
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CommonCodeGenClassMeta.class)
                || clazz.isAssignableFrom(CommonCodeGenFiledMeta.class);
    }

    private void postProcessClassMeta(CommonCodeGenClassMeta classMeta) {
        if (!classMeta.getSource().isEnum()) {
            return;
        }
        CommonCodeGenFiledMeta[] enumConstants = Arrays.stream(classMeta.getFieldMetas())
                .filter(CommonCodeGenFiledMeta::isEnumConstant)
                .toArray(CommonCodeGenFiledMeta[]::new);
        CommonCodeGenFiledMeta[] fieldMetas = Arrays.stream(classMeta.getFieldMetas())
                .filter(commonCodeGenFiledMeta -> !commonCodeGenFiledMeta.isEnumConstant())
                .toArray(CommonCodeGenFiledMeta[]::new);
        classMeta.setEnumConstants(enumConstants);
        classMeta.setFieldMetas(fieldMetas);
    }


    public void postProcessFiledMeta(CommonCodeGenFiledMeta fieldMeta) {
        Field sourceFiled = fieldMeta.getSource();
        if (!sourceFiled.isEnumConstant()) {
            return;
        }
        EnumUtils.getEnumConstant(sourceFiled).ifPresent(enumConstant -> fieldMeta.setEnumFiledValues(resolveEnumFiledValues(fieldMeta, enumConstant)));
    }

    private String[] resolveEnumFiledValues(CommonCodeGenFiledMeta fieldMeta, Enum<?> enumConstant) {
        return Arrays.stream(fieldMeta.getDeclaringClassMeta().getFieldMetas())
                .filter(field -> !field.getIsEnumConstant())
                .map(field -> getEnumFiledValue(enumConstant, field))
                .toArray(String[]::new);
    }

    private String getEnumFiledValue(Enum<?> enumConstant, JavaFieldMeta field) {
        Field enumField = field.getField();
        AccessibleObject.setAccessible(new AccessibleObject[]{enumField}, true);
        try {
            Object value = enumField.get(enumConstant);
            if (value instanceof String) {
                return String.format("\"%s\"", value);
            }
            return value.toString();
        } catch (IllegalAccessException e) {
            if (log.isInfoEnabled()) {
                log.info("获取枚举字段值失败：{}", enumField);
            }
        }
        return null;
    }
}


