package com.wuxp.codegen.languages;

import com.wuxp.codegen.annotation.processors.AnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationToComment;
import com.wuxp.codegen.annotation.processors.javax.NotNullProcessor;
import com.wuxp.codegen.annotation.processors.javax.PatternProcessor;
import com.wuxp.codegen.annotation.processors.javax.SizeProcessor;
import com.wuxp.codegen.annotation.processors.spring.*;
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
 * @see AnnotationProcessor
 */
public final class AnnotationMetaHolder {

    private AnnotationMetaHolder() {
    }

    /**
     * annotationProcessorMap
     */
    private static final Map<Class<? extends Annotation>, AnnotationProcessor<? extends AnnotationToComment, ? extends Annotation>> ANNOTATION_PROCESSOR_MAP = new LinkedHashMap<>();

    static {
        ANNOTATION_PROCESSOR_MAP.put(NotNull.class, new NotNullProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Size.class, new SizeProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Pattern.class, new PatternProcessor());

        RequestMappingProcessor mappingProcessor = new RequestMappingProcessor();
        ANNOTATION_PROCESSOR_MAP.put(RequestMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(GetMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(PostMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(DeleteMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(PutMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(PatchMapping.class, mappingProcessor);

        ANNOTATION_PROCESSOR_MAP.put(CookieValue.class, new CookieValueProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestBody.class, new RequestBodyProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestHeader.class, new RequestHeaderProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestParam.class, new RequestParamProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestPart.class, new RequestPartProcessor());
        ANNOTATION_PROCESSOR_MAP.put(PathVariable.class, new PathVariableProcessor());
    }

    public static <T extends AnnotationToComment, A extends Annotation> Optional<AnnotationProcessor<T, A>> getAnnotationProcessor(Annotation annotation) {
        return getAnnotationProcessor(annotation.annotationType());
    }

    @SuppressWarnings("unchecked")
    public static <T extends AnnotationToComment, A extends Annotation> Optional<AnnotationProcessor<T, A>> getAnnotationProcessor(Class<? extends Annotation> annotationType) {
        return Optional.ofNullable((AnnotationProcessor<T, A>) ANNOTATION_PROCESSOR_MAP.get(annotationType));
    }

    public static void registerAnnotationProcessor(Class<? extends Annotation> annotationType, AnnotationProcessor<? extends AnnotationToComment, ? extends Annotation> processor) {
        ANNOTATION_PROCESSOR_MAP.put(annotationType, processor);
    }
}
