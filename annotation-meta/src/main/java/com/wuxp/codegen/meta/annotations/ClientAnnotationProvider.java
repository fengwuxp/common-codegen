package com.wuxp.codegen.meta.annotations;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;

import java.lang.annotation.Annotation;

/**
 * client 注解提供者
 *
 * @author wuxp
 * @see ClientProviderType
 */
public interface ClientAnnotationProvider {


    /**
     * 通过控制器注解获取对应client provider提供者的注解类型
     *
     * @param annotationType 原始注解类型
     * @return client provider 注解类型
     */
    Class<? extends AnnotationMate> getAnnotation(Class<? extends Annotation> annotationType);

}
