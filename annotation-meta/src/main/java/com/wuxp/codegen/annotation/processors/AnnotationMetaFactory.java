package com.wuxp.codegen.annotation.processors;

import java.lang.annotation.Annotation;

/**
 * 注解元数据工厂
 *
 * @author wuxp
 */
public interface AnnotationMetaFactory<T extends AnnotationToComment, A extends Annotation> {

    /**
     * 处理
     *
     * @param annotation 注解实例
     * @return AnnotationToString的子类
     */
    T factory(A annotation);
}
