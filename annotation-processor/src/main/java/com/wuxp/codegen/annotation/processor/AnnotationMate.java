package com.wuxp.codegen.annotation.processor;

import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


/**
 * 注解的元数据
 *
 * @author wuxp
 */
public interface AnnotationMate extends AnnotationToString, Annotation {


    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return CommonCodeGenAnnotation 实例
     */
    default CommonCodeGenAnnotation toAnnotation(Object annotationOwner) {
        if (annotationOwner == null) {
            return null;
        }
        if (annotationOwner instanceof Class) {
            return this.toAnnotation((Class<?>) annotationOwner);
        } else if (annotationOwner instanceof Field) {
            return this.toAnnotation((Field) annotationOwner);
        } else if (annotationOwner instanceof Method) {
            return this.toAnnotation((Method) annotationOwner);
        } else {
            return this.toAnnotation((Parameter) annotationOwner);
        }
    }

    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return CommonCodeGenAnnotation 实例
     */
    CommonCodeGenAnnotation toAnnotation(Class<?> annotationOwner);

    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return CommonCodeGenAnnotation 实例
     */
    CommonCodeGenAnnotation toAnnotation(Field annotationOwner);

    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return CommonCodeGenAnnotation 实例
     */
    CommonCodeGenAnnotation toAnnotation(Method annotationOwner);

    /**
     * 注解转换
     *
     * @param annotationOwner 注解所有者
     * @return CommonCodeGenAnnotation 实例
     */
    CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner);
}
