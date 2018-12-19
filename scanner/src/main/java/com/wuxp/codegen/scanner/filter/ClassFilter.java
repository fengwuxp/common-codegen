package com.wuxp.codegen.scanner.filter;

@FunctionalInterface
public interface ClassFilter {

    boolean matches(Class<?> clazz);
}
