package com.wuxp.codegen.scanner.filter;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 扫描过滤器
 */
public interface ScannerFilter {

    @SuppressWarnings("unchecked")
    ScannerFilter filterByAnnotation(Class<? extends Annotation>... annotations);

    ScannerFilter filterBySuperClass(Class<?> superClazz);

    ScannerFilter filterByName(String name);

    ScannerFilter filterByCustomFilter(ClassFilter filter);

    List<Class<?>> getClasses();
}
