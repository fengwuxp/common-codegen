package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaParameterMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 参数是否支持生成
 */
public class JavaParameterMatcher implements CodeGenElementMatcher<JavaParameterMeta> {

    private static final List<Class<? extends Annotation>> ATTRIBUTE_ANNOTATIONS = Arrays.asList(
            RequestAttribute.class,
            SessionAttribute.class,
            SessionAttributes.class,
            ModelAttribute.class
    );

    private final Set<Class<? extends Annotation>> ignoreParameterAnnotations;

    public JavaParameterMatcher(Collection<Class<? extends Annotation>> ignoreParameterAnnotations) {
        this.ignoreParameterAnnotations = new HashSet<>(ignoreParameterAnnotations);
        this.ignoreParameterAnnotations.addAll(ATTRIBUTE_ANNOTATIONS);
    }


    @Override
    public boolean matches(JavaParameterMeta parameterMeta) {
        Parameter parameter = parameterMeta.getParameter();
        return ignoreParameterAnnotations.stream().map(parameter::getAnnotation).noneMatch(Objects::nonNull);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaParameterMeta.class);
    }
}
