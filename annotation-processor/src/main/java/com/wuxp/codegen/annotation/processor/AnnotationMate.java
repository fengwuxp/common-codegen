package com.wuxp.codegen.annotation.processor;

import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 注解的元数据
 *
 * @param <T>
 */
public interface AnnotationMate<T extends Annotation> extends AnnotationToString, Annotation {


    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return
     */
    default CommonCodeGenAnnotation toAnnotation(Object annotationOwner) {
        if (annotationOwner == null) {
            return null;
        }
        if (annotationOwner instanceof Class) {
            return this.toAnnotation((Class<?>) annotationOwner);
        } else if (annotationOwner instanceof Field) {
            return this.toAnnotation((Field) annotationOwner);
        } else {
            return this.toAnnotation((Method) annotationOwner);
        }
    }

    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return
     */
     CommonCodeGenAnnotation toAnnotation(Class<?> annotationOwner);

    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return
     */
     CommonCodeGenAnnotation toAnnotation(Field annotationOwner);

    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return
     */
     CommonCodeGenAnnotation toAnnotation(Method annotationOwner);
}
