package com.wuxp.codegen.comment;

import com.wuxp.codegen.core.CodeGenCommentExtractor;
import com.wuxp.codegen.core.CombineCodeGenCommentExtractor;
import com.wuxp.codegen.languages.AnnotationMetaFactoryHolder;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wuxp
 */
public final class CodeGenCommentExtractorFactory {

    private static final CodeGenCommentExtractorFactory INSTANCE = new CodeGenCommentExtractorFactory();

    private final List<CodeGenCommentExtractor> codeGenCommentExtractors;

    private CodeGenCommentExtractorFactory() {
        this.codeGenCommentExtractors = new ArrayList<>();
        initDefaultCommentExtractors();
    }

    public static CodeGenCommentExtractorFactory getInstance() {
        return INSTANCE;
    }

    public CodeGenCommentExtractor factory(Annotation... annotations) {
        if (ObjectUtils.isEmpty(annotations)) {
            return factory(Collections.emptyList());
        }
        return factory(Arrays.asList(annotations));
    }

    public CodeGenCommentExtractor factory(Collection<Annotation> annotations) {
        List<CodeGenCommentExtractor> commentExtractors = annotations.stream()
                .map(AnnotationMetaFactoryHolder::getAnnotationMeta)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        commentExtractors.addAll(codeGenCommentExtractors);
        return CombineCodeGenCommentExtractor.of(commentExtractors);
    }

    public CodeGenCommentExtractor factory(ElementType elementType) {
        return new ClassCodeGenCommentExtractor(elementType);
    }

    public void addCommentExtractor(CodeGenCommentExtractor... extractors) {
        codeGenCommentExtractors.addAll(Arrays.asList(extractors));
    }

    private void initDefaultCommentExtractors() {
        SourceCodeGenCommentExtractor sourceCodeGenCommentExtractor = new SourceCodeGenCommentExtractor();
        addCommentExtractor(sourceCodeGenCommentExtractor);
    }
}
