package com.wuxp.codegen.meta.annotations.factories;

import java.lang.annotation.Annotation;

/**
 * 注解元数据工厂
 *
 * @author wuxp
 */
public interface AnnotationMetaFactory<T extends AnnotationCodeGenCommentExtractor, A extends Annotation> {

    /**
     * 处理
     *
     * @param annotation 注解实例
     * @return AnnotationToString的子类
     */
    T factory(A annotation);
}
