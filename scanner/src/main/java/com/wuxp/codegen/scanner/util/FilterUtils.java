package com.wuxp.codegen.scanner.util;

import java.lang.annotation.Annotation;

public class FilterUtils {

    public static boolean isAnnotationsPresent(Class<? extends Annotation>[] annotations, Class<?> clazz) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (clazz.isAnnotationPresent(annotation)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isClassNameContains(String className, Class<?> clazz) {
        return clazz.getName().contains(className);
    }

    public static boolean isAssignableFrom(Class<?> superClass, Class<?> clazz) {
        return clazz.isAssignableFrom(superClass);
    }
}
