package com.wuxp.codegen.scanner.filter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * 扫描过滤器
 */
public interface ScannerFilter {

    ScannerFilter filterByAnnotation(Class<? extends Annotation>... annotations);

    ScannerFilter filterBySuperClass(Class<?> superClazz);

    ScannerFilter filterByName(String name);

    ScannerFilter filterByCustomFilter(ClassFilter filter);

    Set<Class<?>> getClasses();
}
