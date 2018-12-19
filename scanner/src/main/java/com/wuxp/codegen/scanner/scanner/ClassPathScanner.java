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

    protected Scanner<List<String>, String> fullClassPathScanner = new FullClassPathScanner();

    @Override
    public ScannerFilter scan(String[] packageNames) {

        List<String> classNames = Arrays.stream(packageNames)
                .map(packageName -> this.fullClassPathScanner.scan(packageName))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return new InnerFilter(ClassPathScanner.transform(classNames));
    }


    private static List<Class<?>> transform(List<String> classNames) {
        List<Class<?>> classes = new ArrayList<>();
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

        private List<Class<?>> classes;

        private InnerFilter(List<Class<?>> classes) {
            this.classes = classes;
        }

        @SafeVarargs
        public final ScannerFilter filterByAnnotation(Class<? extends Annotation>... annotations) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isAnnotationsPresent(annotations, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public ScannerFilter filterByName(String name) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isClassNameContains(name, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public ScannerFilter filterBySuperClass(Class<?> superClazz) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (FilterUtils.isAssignableFrom(superClazz, clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public ScannerFilter filterByCustomFilter(ClassFilter classFilter) {
            List<Class<?>> filteredClasses = new ArrayList<>();
            for (Class<?> clazz : classes) {
                if (classFilter.matches(clazz)) {
                    filteredClasses.add(clazz);
                }
            }

            return new InnerFilter(filteredClasses);
        }

        public List<Class<?>> getClasses() {
            return Collections.unmodifiableList(classes);
        }
    }
}
