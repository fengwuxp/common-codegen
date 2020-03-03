package com.wuxp.codegen.annotation.processor;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

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
            return this.toComment((Class<?>) annotationOwner);
        } else if (annotationOwner instanceof Field) {
            return this.toComment((Field) annotationOwner);
        }else if (annotationOwner instanceof Parameter) {
            return this.toComment((Parameter) annotationOwner);
        } else {
            return this.toComment((Method) annotationOwner);
        }
    }

    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return
     */
     String toComment(Class<?> annotationOwner);


    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return
     */
     String toComment(Field annotationOwner);

    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return
     */
    String toComment(Parameter annotationOwner);

    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return
     */
     String toComment(Method annotationOwner);


}
