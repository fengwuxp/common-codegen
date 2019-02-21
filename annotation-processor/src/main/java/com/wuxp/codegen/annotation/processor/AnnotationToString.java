package com.wuxp.codegen.annotation.processor;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 注解 to String
 */
public interface AnnotationToString {


    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return
     */
    default String toComment(Object annotationOwner) {
        if (annotationOwner == null) {
            return null;
        }
        if (annotationOwner instanceof Class) {
            return toComment((Class<?>) annotationOwner);
        } else if (annotationOwner instanceof Field) {
            return toComment((Field) annotationOwner);
        } else {
            return toComment((Method) annotationOwner);
        }
    }

    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return
     */
    default String toComment(Class<?> annotationOwner) {
        return "";
    }


    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return
     */
    default String toComment(Field annotationOwner) {
        return "";
    }

    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return
     */
    default String toComment(Method annotationOwner) {
        return "";
    }


}
