package com.wuxp.codegen.meta.enums;

import com.wuxp.codegen.comment.SourceCodeCommentEnhancer;
import com.wuxp.codegen.core.CodeGenCommentEnhancer;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.core.util.ToggleCaseUtils;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 基于枚举的注释增强
 *
 * @author wuxp
 */
@Slf4j
public class EnumCommentEnhancer implements CodeGenCommentEnhancer, LanguageEnhancedProcessor<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {

    /**
     * 可能是用于描述字段名称
     */
    private static final List<String> DESC_FILED_NAMES = Arrays.asList(
            "desc",
            "introduce",
            "chineseName",
            "englishName",
            "description",
            "name"
    );

    private final Set<String> descFieldNames;

    private final SourceCodeCommentEnhancer sourceCodeCommentEnhancer;

    public EnumCommentEnhancer() {
        this(null);
    }

    public EnumCommentEnhancer(SourceCodeCommentEnhancer sourceCodeCommentEnhancer) {
        this(DESC_FILED_NAMES, sourceCodeCommentEnhancer);

    }

    public EnumCommentEnhancer(Collection<String> descFieldNames, SourceCodeCommentEnhancer sourceCodeCommentEnhancer) {
        this.descFieldNames = new HashSet<>(descFieldNames);
        this.sourceCodeCommentEnhancer = sourceCodeCommentEnhancer;
    }

    @Override
    public String toComment(Field enumField) {
        if (enumField == null || !enumField.isEnumConstant()) {
            return null;
        }
        Class<?> declaringClass = enumField.getDeclaringClass();
        Optional<Enum> optionalEnum = this.getEnumConstant(enumField);
        if (!optionalEnum.isPresent()) {
            return null;
        }
        Enum anEnum = optionalEnum.get();
        return Arrays.stream(declaringClass.getDeclaredFields())
                .filter(field -> !field.isEnumConstant())
                .filter(field -> descFieldNames.contains(field.getName()))
                .map(field -> {
                    String fieldName = field.getName();
                    String methodName = "get" + ToggleCaseUtils.toggleFirstChart(fieldName);
                    try {
                        Method method = ReflectUtils.findDeclaredMethod(declaringClass, methodName, new Class[]{});
                        return method.invoke(anEnum);
                    } catch (Exception ignore) {
                    }
                    // 如果没有获取到注释字段，尝试使用源码获取注释
                    return null;
                })
                .filter(Objects::nonNull)
                .filter(comment -> !anEnum.name().equals(comment))
                .map(Object::toString)
                .findFirst()
                .orElseGet(() -> sourceCodeCommentEnhancer.toComment(enumField));
    }


    @Override
    public CommonCodeGenClassMeta enhancedProcessingClass(CommonCodeGenClassMeta classMeta, JavaClassMeta javaClassMeta) {
        Class<?> clazz = javaClassMeta.getClazz();
        if (!clazz.isEnum()) {
            return classMeta;
        }
        CommonCodeGenFiledMeta[] enumConstants = Arrays.stream(classMeta.getFieldMetas())
                .filter(CommonCodeGenFiledMeta::isEnumConstant)
                .toArray(CommonCodeGenFiledMeta[]::new);
        CommonCodeGenFiledMeta[] fieldMetas = Arrays.stream(classMeta.getFieldMetas())
                .filter(commonCodeGenFiledMeta -> !commonCodeGenFiledMeta.isEnumConstant())
                .toArray(CommonCodeGenFiledMeta[]::new);

        classMeta.setEnumConstants(enumConstants);
        classMeta.setFieldMetas(fieldMetas);
        return classMeta;
    }

    @Override
    public CommonCodeGenFiledMeta enhancedProcessingField(CommonCodeGenFiledMeta fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        Class<?> clazz = classMeta.getClazz();
        if (!clazz.isEnum()) {
            return fieldMeta;
        }
        if (!javaFieldMeta.getField().isEnumConstant()) {
            return fieldMeta;
        }
        Optional<Enum> optionalEnum = getEnumConstant(javaFieldMeta.getField());
        if (!optionalEnum.isPresent()) {
            return null;
        }
        Enum anEnum = optionalEnum.get();
        String[] values = Arrays.stream(classMeta.getFieldMetas())
                .filter(field -> !field.getIsEnumConstant())
                .map(field -> {
                    Field enumField = field.getField();
                    AccessibleObject.setAccessible(new AccessibleObject[]{enumField}, true);
                    try {
                        Object value = enumField.get(anEnum);
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
                })
                .toArray(String[]::new);
        fieldMeta.setEnumFiledValues(values);
        return fieldMeta;
    }

    private Optional<Enum> getEnumConstant(Field enumField) {
        Class<?> declaringClass = enumField.getDeclaringClass();
        return Arrays.stream(declaringClass.getEnumConstants())
                .map(enumConstant -> (Enum) enumConstant)
                .filter(enumConstant -> enumField.getName().equals(enumConstant.name()))
                .findFirst();
    }
}
