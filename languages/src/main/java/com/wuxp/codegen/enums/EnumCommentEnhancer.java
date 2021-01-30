package com.wuxp.codegen.enums;

import com.wuxp.codegen.comment.SourceCodeCommentEnhancer;
import com.wuxp.codegen.core.CodeGenCommentEnhancer;
import com.wuxp.codegen.core.util.ToggleCaseUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 基于枚举的注释增强
 *
 * @author wuxp
 */
public class EnumCommentEnhancer implements CodeGenCommentEnhancer {

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
        Optional<Enum> optionalEnum = Arrays.stream(declaringClass.getEnumConstants())
                .map(enumConstant -> (Enum) enumConstant)
                .filter(enumConstant -> enumField.getName().equals(enumConstant.name()))
                .findFirst();
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
                .orElseGet(()-> sourceCodeCommentEnhancer.toComment(enumField));
    }
}
