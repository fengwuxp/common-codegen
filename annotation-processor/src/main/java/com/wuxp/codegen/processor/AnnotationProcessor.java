package com.wuxp.codegen.processor;

import java.lang.annotation.Annotation;

/**
 * 注解处理器
 */
@FunctionalInterface
public interface AnnotationProcessor<T> {

   <A  extends Annotation> T  process(A annotation);
}
