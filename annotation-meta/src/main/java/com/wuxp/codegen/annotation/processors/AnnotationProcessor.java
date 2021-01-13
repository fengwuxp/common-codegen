package com.wuxp.codegen.annotation.processors;

import java.lang.annotation.Annotation;

/**
 * 注解装换处理器，
 *
 * @author wuxp
 */
public interface AnnotationProcessor<T extends AnnotationToString, A extends Annotation> {

    /**
     * 处理
     *
     * @param annotation 注解实例
     * @return AnnotationToString的子类
     */
    T process(A annotation);
}
