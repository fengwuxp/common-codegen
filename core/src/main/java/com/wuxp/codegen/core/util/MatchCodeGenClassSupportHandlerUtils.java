package com.wuxp.codegen.core.util;

import com.wuxp.codegen.core.CodeGenClassSupportHandler;

import java.util.List;
import java.util.Optional;

public final class MatchCodeGenClassSupportHandlerUtils {

    private MatchCodeGenClassSupportHandlerUtils() {
        throw new AssertionError();
    }

    public static <T extends CodeGenClassSupportHandler> T matchesHandler(List<T> handles, Class<?> clazz) {
        return matchesHandlerOfNullable(handles, clazz).orElse(null);
    }

    public static <T extends CodeGenClassSupportHandler> Optional<T> matchesHandlerOfNullable(List<T> handles, Class<?> clazz) {
        return handles.stream()
                .filter(support -> support.supports(clazz))
                .findFirst();
    }
}
