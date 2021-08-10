package com.wuxp.codegen.meta.enums;

import com.wuxp.codegen.comment.SourceCodeGenCommentExtractor;
import com.wuxp.codegen.core.CodeGenCommentExtractor;
import com.wuxp.codegen.core.util.ToggleCaseUtils;
import com.wuxp.codegen.meta.util.EnumUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 基于枚举的注释增强
 *
 * @author wuxp
 */
@Slf4j
public class EnumCodeGenCommentExtractor implements CodeGenCommentExtractor {

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

    private final SourceCodeGenCommentExtractor sourceCodeCommentEnhancer;

    public EnumCodeGenCommentExtractor() {
        this(null);
    }

    public EnumCodeGenCommentExtractor(SourceCodeGenCommentExtractor sourceCodeCommentEnhancer) {
        this(DESC_FILED_NAMES, sourceCodeCommentEnhancer);
    }

    public EnumCodeGenCommentExtractor(Collection<String> descFieldNames, SourceCodeGenCommentExtractor sourceCodeCommentEnhancer) {
        this.descFieldNames = new HashSet<>(descFieldNames);
        this.sourceCodeCommentEnhancer = sourceCodeCommentEnhancer;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public String toComment(Field enumField) {
        if (enumField == null || !enumField.isEnumConstant()) {
            return null;
        }
        Class<?> declaringClass = enumField.getDeclaringClass();
        Optional<Enum> optionalEnum = EnumUtils.getEnumConstant(enumField);
        if (!optionalEnum.isPresent()) {
            return null;
        }
        Enum<?> enumConstant = optionalEnum.get();
        return Arrays.stream(declaringClass.getDeclaredFields())
                .filter(field -> !field.isEnumConstant())
                .filter(field -> descFieldNames.contains(field.getName()))
                .map(field -> getComment(declaringClass, enumConstant, field))
                .filter(Objects::nonNull)
                .filter(comment -> !enumConstant.name().equals(comment))
                .findFirst()
                .orElseGet(() -> sourceCodeCommentEnhancer.toComment(enumField));
    }

    private String getComment(Class<?> declaringClass, Enum<?> enumConstant, Field field) {
        String fieldName = field.getName();
        String methodName = String.format("get%s", ToggleCaseUtils.toggleFirstChart(fieldName));
        try {
            Method method = ReflectUtils.findDeclaredMethod(declaringClass, methodName, new Class[]{});
            return (String) method.invoke(enumConstant);
        } catch (Exception ignore) {
        }
        // 如果没有获取到注释字段，尝试使用源码获取注释
        return null;
    }


}
