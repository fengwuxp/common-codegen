package com.wuxp.codegen.transform;


import com.wuxp.codegen.annotation.processors.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 注解transformer
 * @author wuxp
 */
public interface AnnotationCodeGenTransformer<T extends CommonCodeGenAnnotation, A extends AnnotationMate> {


    /**
     * 将注解转换为<T>
     *
     * @param annotationMate
     * @param annotationOwner
     * @return string code
     */
    default T transform(A annotationMate, Object annotationOwner) {
        if (annotationOwner == null) {
            return null;
        }
        if (annotationOwner instanceof Class) {
            return this.transform(annotationMate, (Class<?>) annotationOwner);
        } else if (annotationOwner instanceof Field) {
            return this.transform(annotationMate, (Field) annotationOwner);
        } else {
            return this.transform(annotationMate, (Method) annotationOwner);
        }
    }

    /**
     * 将注解转换为<T>
     *
     * @param annotationMate
     * @param annotationOwner
     * @return string code
     */
    default T transform(A annotationMate, Class<?> annotationOwner) {
        return null;
    }

    /**
     * 将注解转换为<T>
     *
     * @param annotationMate
     * @param annotationOwner
     * @return string code
     */
    default T transform(A annotationMate, Method annotationOwner) {
        return null;
    }

    /**
     * 将注解转换为<T>
     *
     * @param annotationMate
     * @param annotationOwner
     * @return string code
     */
    default T transform(A annotationMate, Field annotationOwner) {
        return null;
    }

    /**
     * 将注解转换为<T>
     *
     * @param annotationMate
     * @param annotationOwner
     * @return string code
     */
    default T transform(A annotationMate, Parameter annotationOwner) {
        return null;
    }
}
