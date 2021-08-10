package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;

public interface CodeGenTypeElementMatcher extends CodeGenElementMatcher<Class<?>> {

    @Override
    default boolean supports(Class<?> clazz) {
        return Class.class == clazz;
    }
}
