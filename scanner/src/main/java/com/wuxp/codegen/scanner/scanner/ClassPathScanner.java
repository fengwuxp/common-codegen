package com.wuxp.codegen.scanner.scanner;

import com.wuxp.codegen.scanner.filter.ClassFilter;
import com.wuxp.codegen.scanner.filter.ScannerFilter;
import com.wuxp.codegen.scanner.util.FilterUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 类路径包扫描者
 */
@Slf4j
public class ClassPathScanner implements Scanner<ScannerFilter, String[]> {

    protected Scanner<Set<String>, String> fullClassPathScanner = new FullClassPathScanner();

    @Override
    public ScannerFilter scan(String[] packageNames) {

        Set<String> classNames = Arrays.stream(packageNames)
                .map(packageName -> this.fullClassPathScanner.scan(packageName))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return new InnerFilter(ClassPathScanner.transform(classNames));
    }


    private static Set<Class<?>> transform(Set<String> classNames) {
        Set<Class<?>> classes = new HashSet<>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (String className : classNames) {
            try {
                classes.add(contextClassLoader.loadClass(className));
            } catch (Throwable exception) {
                if (log.isDebugEnabled()) {
                    log.error("Class not found: ", exception);
                }
            }
        }

        return classes;
    }


    private class InnerFilter implements ScannerFilter {

        private Set<Class<?>> classes;

        private InnerFilter(Set<Class<?>> classes) {
            this.classes = classes;
        }

        @SafeVarargs
        public final ScannerFilter filterByAnnotation(Class<? extends Annotation>... annotations) {
            Set<Class<?>> filteredClasses = new HashSet<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isAnnotationsPresent(annotations, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public ScannerFilter filterByName(String name) {
            Set<Class<?>> filteredClasses = new HashSet<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isClassNameContains(name, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public ScannerFilter filterBySuperClass(Class<?> superClazz) {
            Set<Class<?>> filteredClasses = new HashSet<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isAssignableFrom(superClazz, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public ScannerFilter filterByCustomFilter(ClassFilter classFilter) {
            Set<Class<?>> filteredClasses = new HashSet<>();
            for (Class<?> clazz : classes) {
                if (classFilter.matches(clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public Set<Class<?>> getClasses() {
            return Collections.unmodifiableSet(classes);
        }
    }
}
