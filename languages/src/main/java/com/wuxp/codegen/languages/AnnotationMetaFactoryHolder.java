package com.wuxp.codegen.languages;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wuxp.codegen.meta.annotations.factories.AnnotationCodeGenCommentExtractor;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.jackson.JsonFormatMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.javax.NotBlanklMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.javax.NotNullMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.javax.PatternMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.javax.SizeMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.CookieValueMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.PathVariableMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestBodyMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestHeaderMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestParamMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestPartMetaFactory;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
        ANNOTATION_META_FACTORIES.put(NotBlank.class, new NotBlanklMetaFactory());
        ANNOTATION_META_FACTORIES.put(Size.class, new SizeMetaFactory());
        ANNOTATION_META_FACTORIES.put(Pattern.class, new PatternMetaFactory());
        ANNOTATION_META_FACTORIES.put(JsonFormat.class, new JsonFormatMetaFactory());

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
