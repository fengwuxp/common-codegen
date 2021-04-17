package com.wuxp.codegen.annotation;

import com.wuxp.codegen.annotation.processors.AnnotationMate;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author wuxp
 */
public abstract class AbstractClientAnnotationProvider implements ClientAnnotationProvider {

    protected final Map<Class<? extends Annotation>, Class<? extends AnnotationMate>> annotationMap;

    protected AbstractClientAnnotationProvider(Map<Class<? extends Annotation>, Class<? extends AnnotationMate>> annotationMap) {
        this.annotationMap = annotationMap;
    }

    @Override
    public Class<? extends AnnotationMate> getAnnotation(Class<? extends Annotation> annotationType) {
        return annotationMap.get(annotationType);
    }

    protected AbstractClientAnnotationProvider addAnnotation(Class<? extends Annotation> annotationType, Class<? extends AnnotationMate> annotationMeta) {
        this.annotationMap.put(annotationType, annotationMeta);
        return this;
    }
}
