package com.wuxp.codegen.annotation.processor;

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
     * @param annotation
     * @return
     */
    T process(A annotation);
}
