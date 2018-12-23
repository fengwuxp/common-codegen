package com.wuxp.codegen.transform;


import java.lang.annotation.Annotation;

/**
 * 注解transformer
 */
public interface AnnotationCodeGenTransformer<T,A extends Annotation> {

    /**
     * 将注解转换为不同平台的字符串代码
     *
     * @param annotations
     * @return string code
     */
    T transform(A[] annotations);
}
