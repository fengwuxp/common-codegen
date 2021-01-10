package com.wuxp.codegen.annotation.processor;

import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.util.StringUtils;

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

    /**
     * 获取参数名称
     *
     * @param annotationOwner 参数对象
     * @return 参数名称
     */
    default String getParameterName(Parameter annotationOwner) {

        return getParameterName(annotationOwner, null);
    }

    /**
     * 获取参数名称
     *
     * @param annotationOwner 参数对象
     * @param markName        标记名称
     * @return 参数名称
     */
    default String getParameterName(Parameter annotationOwner, String markName) {

        if (StringUtils.hasText(markName)) {
            return markName;
        }

        return JavaClassParser.getParameterName(annotationOwner);
    }
}
