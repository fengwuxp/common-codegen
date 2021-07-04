package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wuxp
 */
public class ApiAnnotationCodeGenElementMatcher implements CodeGenElementMatcher<Class<?>> {

    private final Set<Class<? extends Annotation>> annotationTypes;

    public ApiAnnotationCodeGenElementMatcher(Collection<Class<? extends Annotation>> annotationTypes) {
        this.annotationTypes = new HashSet<>(annotationTypes);
    }

    @Override
    public boolean matches(Class<?> source) {
        return matches(source.getAnnotations());
    }

    private boolean matches(Annotation... annotations) {
        return Arrays.stream(annotations).map(Annotation::getClass).anyMatch(this.annotationTypes::contains);
    }

}
