package com.wuxp.codegen.languages;

import com.wuxp.codegen.meta.annotations.factories.AnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationCodeGenCommentExtractor;
import com.wuxp.codegen.meta.annotations.factories.javax.NotNullMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.javax.PatternMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.javax.SizeMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wuxp
 * @see AnnotationMetaFactory
 */
public final class AnnotationMetaFactoryHolder {

    private AnnotationMetaFactoryHolder() {
        throw new AssertionError();
    }

    /**
     * 注解元数据工厂缓存
     */
    private static final Map<Class<? extends Annotation>, AnnotationMetaFactory<? extends AnnotationCodeGenCommentExtractor, ? extends Annotation>> ANNOTATION_META_FACTORIES = new LinkedHashMap<>();

    static {
        ANNOTATION_META_FACTORIES.put(NotNull.class, new NotNullMetaFactory());
        ANNOTATION_META_FACTORIES.put(Size.class, new SizeMetaFactory());
        ANNOTATION_META_FACTORIES.put(Pattern.class, new PatternMetaFactory());

        RequestMappingMetaFactory mappingProcessor = new RequestMappingMetaFactory();
        ANNOTATION_META_FACTORIES.put(RequestMapping.class, mappingProcessor);
        ANNOTATION_META_FACTORIES.put(GetMapping.class, mappingProcessor);
        ANNOTATION_META_FACTORIES.put(PostMapping.class, mappingProcessor);
        ANNOTATION_META_FACTORIES.put(DeleteMapping.class, mappingProcessor);
        ANNOTATION_META_FACTORIES.put(PutMapping.class, mappingProcessor);
        ANNOTATION_META_FACTORIES.put(PatchMapping.class, mappingProcessor);

        ANNOTATION_META_FACTORIES.put(CookieValue.class, new CookieValueMetaFactory());
        ANNOTATION_META_FACTORIES.put(RequestBody.class, new RequestBodyMetaFactory());
        ANNOTATION_META_FACTORIES.put(RequestHeader.class, new RequestHeaderMetaFactory());
        ANNOTATION_META_FACTORIES.put(RequestParam.class, new RequestParamMetaFactory());
        ANNOTATION_META_FACTORIES.put(RequestPart.class, new RequestPartMetaFactory());
        ANNOTATION_META_FACTORIES.put(PathVariable.class, new PathVariableMetaFactory());
    }

    public static <T extends AnnotationCodeGenCommentExtractor, A extends Annotation> Optional<AnnotationMetaFactory<T, A>> getAnnotationMetaFactory(Annotation annotation) {
        return getAnnotationMetaFactory(annotation.annotationType());
    }

    @SuppressWarnings("unchecked")
    public static <T extends AnnotationCodeGenCommentExtractor, A extends Annotation> Optional<AnnotationMetaFactory<T, A>> getAnnotationMetaFactory(Class<? extends Annotation> annotationType) {
        return Optional.ofNullable((AnnotationMetaFactory<T, A>) ANNOTATION_META_FACTORIES.get(annotationType));
    }

    public static void registerAnnotationMetaFactory(Class<? extends Annotation> annotationType, AnnotationMetaFactory<? extends AnnotationCodeGenCommentExtractor, ? extends Annotation> factory) {
        ANNOTATION_META_FACTORIES.put(annotationType, factory);
    }

    public static Optional<AnnotationCodeGenCommentExtractor> getAnnotationMeta(Annotation annotation) {
        return AnnotationMetaFactoryHolder.getAnnotationMetaFactory(annotation)
                .map(processor -> processor.factory(annotation));
    }
}
