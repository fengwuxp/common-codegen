package com.wuxp.codegen.annotations;

import com.wuxp.codegen.core.parser.LanguageAnnotationParser;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.languages.AnnotationMetaFactoryHolder;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMetaFactory;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

/**
 * @author wuxp
 */
public final class DefaultLanguageAnnotationParser implements LanguageAnnotationParser {

    private final List<LanguageDefinitionPostProcessor<? extends CommonBaseMeta>> postProcessors;

    public DefaultLanguageAnnotationParser(List<LanguageDefinitionPostProcessor<? extends CommonBaseMeta>> postProcessors) {
        this.postProcessors = postProcessors;
    }

    /**
     * 转换注解列表
     *
     * @param annotationOwner 注解持有者
     * @return 用于生成的注解列表
     */
    @Override
    public CommonCodeGenAnnotation[] parseAnnotatedElement(AnnotatedElement annotationOwner) {
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

    private List<CommonCodeGenAnnotation> getCommonCodeGenAnnotations(Annotation annotation, AnnotatedElement annotationOwner) {
        Optional<AnnotationMetaFactory<AnnotationMate, Annotation>> annotationMetaFactory = AnnotationMetaFactoryHolder.getAnnotationMetaFactory(annotation);
        if (!annotationMetaFactory.isPresent()) {
            return Collections.emptyList();
        }
        CommonCodeGenAnnotation toAnnotation = annotationMetaFactory.get().factory(annotation).toAnnotation(annotationOwner);
        if (toAnnotation == null) {
            return Collections.emptyList();
        }
        toAnnotation.setSource(annotation);
        toAnnotation.setAnnotationOwner(annotationOwner);
        List<CommonCodeGenAnnotation> toAnnotations = new ArrayList<>();
        postProcess(toAnnotation);
        toAnnotations.add(toAnnotation);
        toAnnotations.addAll(getCommonCodeGenAnnotations(toAnnotation));
        return toAnnotations;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void postProcess(CommonCodeGenAnnotation meta) {
        for (LanguageDefinitionPostProcessor processor : postProcessors) {
            if (processor.supports(meta.getClass())) {
                processor.postProcess(meta);
            }
        }
    }

    private List<CommonCodeGenAnnotation> getCommonCodeGenAnnotations(CommonCodeGenAnnotation toAnnotation) {
        return toAnnotation.getAssociatedAnnotations() == null ? Collections.emptyList() : toAnnotation.getAssociatedAnnotations();
    }

}
