package com.wuxp.codegen.enums;

import com.wuxp.codegen.core.CodeGenCommentEnhancer;
import com.wuxp.codegen.core.util.ToggleCaseUtils;
import org.springframework.cglib.core.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

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
            "name",
            "desc",
            "introduce",
            "chineseName",
            "englishName",
            "description"
    );

    private final Set<String> descFieldNames;

    public EnumCommentEnhancer() {
        this(DESC_FILED_NAMES);
    }

    public EnumCommentEnhancer(Collection<String> descFieldNames) {
        this.descFieldNames = new HashSet<>(descFieldNames);
    }

    @Override
    public String toComment(Field annotationOwner) {
        if (annotationOwner == null || !annotationOwner.isEnumConstant()) {
            return null;
        }
        Class<?> declaringClass = annotationOwner.getDeclaringClass();
        Optional<Enum> optionalEnum = Arrays.stream(declaringClass.getEnumConstants())
                .map(enumConstant -> (Enum) enumConstant)
                .filter(enumConstant -> annotationOwner.getName().equals(enumConstant.name()))
                .findFirst();
        if (!optionalEnum.isPresent()) {
            return null;
        }
        Enum anEnum = optionalEnum.get();
        return Arrays.stream(declaringClass.getDeclaredFields())
                .filter(field -> !field.isEnumConstant()).map(Field::getName)
                .filter(descFieldNames::contains)
                .map(fieldName -> {
                    String methodName = "get" + ToggleCaseUtils.toggleFirstChart(fieldName);
                    try {
                        Method method = ReflectUtils.findDeclaredMethod(declaringClass, methodName, new Class[]{});
                        return method.invoke(anEnum);
                    } catch (Exception ignore) {
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .filter(comment -> !anEnum.name().equals(comment))
                .map(Object::toString)
                .collect(Collectors.joining("、"));
    }
}
