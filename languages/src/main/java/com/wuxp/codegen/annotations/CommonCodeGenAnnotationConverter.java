package com.wuxp.codegen.annotations;

import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.languages.AnnotationMetaFactoryHolder;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMetaFactory;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;

/**
 * @author wuxp
 */
public class CommonCodeGenAnnotationConverter {


    private final LanguageEnhancedProcessor<? extends CommonCodeGenClassMeta, ? extends CommonCodeGenMethodMeta, ? extends CommonCodeGenFiledMeta> languageEnhancedProcessor;

    private CommonCodeGenAnnotationConverter(LanguageEnhancedProcessor<? extends CommonCodeGenClassMeta, ? extends CommonCodeGenMethodMeta, ? extends CommonCodeGenFiledMeta> languageEnhancedProcessor) {
        this.languageEnhancedProcessor = languageEnhancedProcessor;
    }

    public static CommonCodeGenAnnotationConverter of(LanguageEnhancedProcessor<? extends CommonCodeGenClassMeta, ? extends CommonCodeGenMethodMeta, ? extends CommonCodeGenFiledMeta> languageEnhancedProcessor) {
        return new CommonCodeGenAnnotationConverter(languageEnhancedProcessor);
    }

    /**
     * 转换注解列表
     *
     * @param annotationOwner 注解持有者
     * @return 用于生成的注解列表
     */
    public CommonCodeGenAnnotation[] convert(AnnotatedElement annotationOwner) {
        if (annotationOwner == null) {
            return new CommonCodeGenAnnotation[0];
        }
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
        toAnnotations.add(enhancedProcessingAnnotation(toAnnotation, annotation, annotationOwner));
        toAnnotations.addAll(getCommonCodeGenAnnotations(toAnnotation));
        return toAnnotations;
    }

    private List<CommonCodeGenAnnotation> getCommonCodeGenAnnotations(CommonCodeGenAnnotation toAnnotation) {
        return toAnnotation.getAssociatedAnnotations() == null ? Collections.emptyList() : toAnnotation.getAssociatedAnnotations();
    }

    private CommonCodeGenAnnotation enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, Annotation annotation,
                                                                 Object annotationOwner) {
        return this.languageEnhancedProcessor.enhancedProcessingAnnotation(codeGenAnnotation, annotation, annotationOwner);
    }
}
