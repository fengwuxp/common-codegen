package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 用于匹配参数上的Spring相关注解
 *
 * @author wuxp
 * @see RequestAttribute
 * @see SessionAttribute
 * @see SessionAttributes
 * @see ModelAttribute
 */
public class IgnoreMethodParameterMatchingStrategy implements CodeGenMatchingStrategy {

    private static final List<Class<? extends Annotation>> ATTRIBUTE_ANNOTATIONS = Arrays.asList(
            RequestAttribute.class,
            SessionAttribute.class,
            SessionAttributes.class,
            ModelAttribute.class
    );

    private final Set<Class<? extends Annotation>> ignoreParameterAnnotations;

    public static IgnoreMethodParameterMatchingStrategy of(Class<? extends Annotation>... ignoreParameterAnnotations) {
        return new IgnoreMethodParameterMatchingStrategy(Arrays.asList(ignoreParameterAnnotations));
    }

    public static IgnoreMethodParameterMatchingStrategy of(Collection<Class<? extends Annotation>> ignoreParameterAnnotations) {
        return new IgnoreMethodParameterMatchingStrategy(ignoreParameterAnnotations);
    }

    private IgnoreMethodParameterMatchingStrategy(Collection<Class<? extends Annotation>> ignoreParameterAnnotations) {
        this.ignoreParameterAnnotations = new HashSet<>(ignoreParameterAnnotations);
        this.ignoreParameterAnnotations.addAll(ATTRIBUTE_ANNOTATIONS);
    }

    @Override
    public boolean isMatchParameter(JavaMethodMeta javaMethodMeta, Parameter parameter) {
        return ignoreParameterAnnotations.stream().map(parameter::getAnnotation).noneMatch(Objects::nonNull);
    }
}
