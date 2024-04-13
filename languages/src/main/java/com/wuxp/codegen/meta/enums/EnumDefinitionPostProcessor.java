package com.wuxp.codegen.meta.enums;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.meta.util.EnumUtils;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class EnumDefinitionPostProcessor implements LanguageDefinitionPostProcessor<CommonBaseMeta> {

    @Override
    public void postProcess(CommonBaseMeta meta) {
        Class<?> clazz = meta.getClass();
        if (JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenClassMeta.class)) {
            postProcessClassMeta((CommonCodeGenClassMeta) meta);
        }
        if (JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenFiledMeta.class)) {
            postProcessFiledMeta((CommonCodeGenFiledMeta) meta);
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenClassMeta.class) ||
                JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenFiledMeta.class);
    }

    private void postProcessClassMeta(CommonCodeGenClassMeta classMeta) {
        Class<?> source = classMeta.getSource();
        if (source == null || !source.isEnum()) {
            return;
        }
        CommonCodeGenFiledMeta[] enumConstants = Arrays.stream(classMeta.getFieldMetas())
                .filter(CommonCodeGenFiledMeta::isEnumConstant)
                .toArray(CommonCodeGenFiledMeta[]::new);
        CommonCodeGenFiledMeta[] fieldMetas = Arrays.stream(classMeta.getFieldMetas())
                .filter(commonCodeGenFiledMeta -> !commonCodeGenFiledMeta.isEnumConstant())
                .toArray(CommonCodeGenFiledMeta[]::new);
        if (!ObjectUtils.isEmpty(enumConstants)) {
            // 由于通过事件解析，可能出现重复处理，此处仅在获取到数据是进行设置
            classMeta.setEnumConstants(enumConstants);
            classMeta.setFieldMetas(fieldMetas);
        }
    }


    public void postProcessFiledMeta(CommonCodeGenFiledMeta fieldMeta) {
        Field sourceFiled = fieldMeta.getSource();
        if (sourceFiled == null || !sourceFiled.isEnumConstant()) {
            return;
        }
        EnumUtils.getEnumConstant(sourceFiled).ifPresent(enumConstant -> fieldMeta.setEnumFiledValues(resolveEnumFiledValues(fieldMeta, enumConstant)));
    }

    private String[] resolveEnumFiledValues(CommonCodeGenFiledMeta fieldMeta, Enum<?> enumConstant) {
        return Arrays.stream(fieldMeta.getDeclaringClassMeta().getFieldMetas())
                .filter(field -> !field.getIsEnumConstant())
                .map(field -> getEnumFiledValue(enumConstant, field))
                .filter(Objects::nonNull)
                // TODO 枚举字段名称判断优化
                .filter(name -> !name.contains("@"))
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
            return value == null ? null : value.toString();
        } catch (IllegalAccessException e) {
            if (log.isInfoEnabled()) {
                log.info("获取枚举字段值失败：{}", enumField);
            }
        }
        return null;
    }
}


