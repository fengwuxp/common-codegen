package com.wuxp.codegen.annotations;

import com.wuxp.codegen.core.parser.enhance.SimpleLanguageDefinitionPostProcessor;
import com.wuxp.codegen.languages.AnnotationMetaFactoryHolder;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMetaFactory;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

/**
 * @author wuxp
 */
public final class LanguageAnnotationParser implements SimpleLanguageDefinitionPostProcessor<CommonCodeGenAnnotation> {

    private static final LanguageAnnotationParser INSTANCE = new LanguageAnnotationParser();

    public static LanguageAnnotationParser getInstance() {
        return INSTANCE;
    }

    /**
     * 转换注解列表
     *
     * @param annotationOwner 注解持有者
     * @return 用于生成的注解列表
     */
    public CommonCodeGenAnnotation[] parse(AnnotatedElement annotationOwner) {
        Annotation[] annotations = annotationOwner.getAnnotations();
        if (ObjectUtils.isEmpty(annotations)) {
            return new CommonCodeGenAnnotation[0];
        }
        return Arrays.stream(annotations)
                .map(annotation -> getCommonCodeGenAnnotations(annotation, annotationOwner))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .distinct()
                .toArray(CommonCodeGenAnnotation[]::new);
    }

    private List<CommonCodeGenAnnotation> getCommonCodeGenAnnotations(Annotation annotation, Object annotationOwner) {
        Optional<AnnotationMetaFactory<AnnotationMate, Annotation>> annotationMetaFactory = AnnotationMetaFactoryHolder.getAnnotationMetaFactory(annotation);
        if (!annotationMetaFactory.isPresent()) {
            return Collections.emptyList();
        }
        CommonCodeGenAnnotation toAnnotation = annotationMetaFactory.get().factory(annotation).toAnnotation(annotationOwner);
        if (toAnnotation == null) {
            return Collections.emptyList();
        }
        List<CommonCodeGenAnnotation> toAnnotations = new ArrayList<>();
        toAnnotations.add(postProcess(toAnnotation));
        toAnnotations.addAll(getCommonCodeGenAnnotations(toAnnotation));
        return toAnnotations;
    }

    private List<CommonCodeGenAnnotation> getCommonCodeGenAnnotations(CommonCodeGenAnnotation toAnnotation) {
        return toAnnotation.getAssociatedAnnotations() == null ? Collections.emptyList() : toAnnotation.getAssociatedAnnotations();
    }

}
