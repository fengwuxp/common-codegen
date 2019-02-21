package com.wuxp.codegen.annotation.processor;

import java.lang.annotation.Annotation;

/**
 * 注解处理器
 */
public interface AnnotationProcessor<T extends AnnotationToString,A extends Annotation> {

    T  process(A annotation);
}
