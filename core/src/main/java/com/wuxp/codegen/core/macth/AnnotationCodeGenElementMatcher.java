package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 根据 {@link AnnotatedElement} 上是否存在 预期的{@link #annotationTypes}注解类型
 *
 * @author wuxp
 */
public class AnnotationCodeGenElementMatcher implements CodeGenElementMatcher<AnnotatedElement> {

    private final Set<Class<? extends Annotation>> annotationTypes;

    public AnnotationCodeGenElementMatcher(Collection<Class<? extends Annotation>> annotationTypes) {
        this.annotationTypes = new HashSet<>(annotationTypes);
    }

    @Override
    public boolean matches(AnnotatedElement source) {
        return matches(source.getAnnotations());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Annotation.class);
    }

    private boolean matches(Annotation... annotations) {
        return Arrays.stream(annotations).map(Annotation::getClass).anyMatch(this.annotationTypes::contains);
    }
}
