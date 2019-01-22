package com.wuxp.codegen.annotation.processor;

import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import java.lang.annotation.Annotation;


/**
 * 注解的元数据
 *
 * @param <T>
 */
public interface AnnotationMate<T extends Annotation> extends AnnotationToString ,Annotation {


    /**
     * 注解转换
     * @param <A>
     * @return
     */
    <A extends CommonCodeGenAnnotation> A toAnnotation();
}
