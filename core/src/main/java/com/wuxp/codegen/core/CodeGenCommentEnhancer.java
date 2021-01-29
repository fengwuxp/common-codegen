package com.wuxp.codegen.core;


import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 代码生成用于增强注释获取的增强器
 *
 * @author wuxp
 */
public interface CodeGenCommentEnhancer {


    /**
     * 生成注释
     *
     * @param annotationOwner 注释的owner
     * @return 从owner得到的注释
     */
    default String toComment(AnnotatedElement annotationOwner) {
        if (annotationOwner == null) {
            return null;
        }
        if (annotationOwner instanceof Class) {
            return this.toComment((Class<?>) annotationOwner);
        } else if (annotationOwner instanceof Field) {
            return this.toComment((Field) annotationOwner);
        } else if (annotationOwner instanceof Parameter) {
            return this.toComment((Parameter) annotationOwner);
        } else {
            return this.toComment((Method) annotationOwner);
        }
    }

    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return 从owner得到的注释
     */
    default String toComment(Class<?> annotationOwner) {
        return null;
    }


    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return 从owner得到的注释
     */
    default String toComment(Field annotationOwner) {
        return null;
    }

    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return 从owner得到的注释
     */
    default String toComment(Parameter annotationOwner) {
        return null;
    }

    /**
     * 转换为注释
     *
     * @param annotationOwner 注解所有者
     * @return 从owner得到的注释
     */
    default String toComment(Method annotationOwner) {
        return null;
    }
}
