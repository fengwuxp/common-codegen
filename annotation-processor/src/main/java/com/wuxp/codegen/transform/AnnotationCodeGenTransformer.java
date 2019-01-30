package com.wuxp.codegen.transform;


import java.lang.annotation.Annotation;

/**
 * 注解transformer
 */
@FunctionalInterface
public interface AnnotationCodeGenTransformer<T, A extends Annotation> {

    /**
     * 将注解转换为<T>
     *
     * @param annotations
     * @return string code
     */
    T transform(A[] annotations);
}
