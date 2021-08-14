package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaBaseMeta;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 根据 {@link AnnotatedElement} 上是否存在需要排除 {@link #excludeAnnotationsTypes} 注解类型
 *
 * @author wuxp
 */
public class ExcludeAnnotationCodeGenElementMatcher implements CodeGenElementMatcher<Object> {

    private final Set<Class<? extends Annotation>> excludeAnnotationsTypes;

    public ExcludeAnnotationCodeGenElementMatcher(Collection<Class<? extends Annotation>> excludeAnnotationsTypes) {
        this.excludeAnnotationsTypes = new HashSet<>(excludeAnnotationsTypes);
    }

    @Override
    public boolean matches(Object source) {
        Annotation[] annotations;
        if (source instanceof AnnotatedElement) {
            annotations = ((AnnotatedElement) source).getAnnotations();
        } else if (source instanceof JavaBaseMeta) {
            annotations = ((JavaBaseMeta) source).getAnnotations();
        } else {
            annotations = new Annotation[0];
        }
        return !matches(annotations);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    private boolean matches(Annotation... annotations) {
        return Arrays.stream(annotations).map(Annotation::annotationType).anyMatch(this.excludeAnnotationsTypes::contains);
    }
}
