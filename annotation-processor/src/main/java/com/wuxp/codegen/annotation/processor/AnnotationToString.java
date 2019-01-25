package com.wuxp.codegen.annotation.processor;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 注解 to String
 */
public interface AnnotationToString {

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
