package com.wuxp.codegen.resolve;


import java.lang.annotation.Annotation;

/**
 * 用于解析注解
 */
public interface AnnotationResolve<T> {


    T resolve(Annotation annotation);

}
