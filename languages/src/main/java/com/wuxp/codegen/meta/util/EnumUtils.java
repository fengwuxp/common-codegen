package com.wuxp.codegen.meta.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author wuxp
 */
public final class EnumUtils {

    private EnumUtils() {
        throw new AssertionError();
    }

    @SuppressWarnings("rawtypes")
    public static Optional<Enum> getEnumConstant(Field enumField) {
        Class<?> declaringClass = enumField.getDeclaringClass();
        return Arrays.stream(declaringClass.getEnumConstants())
                .map(Enum.class::cast)
                .filter(enumConstant -> enumField.getName().equals(enumConstant.name()))
                .findFirst();
    }
}
