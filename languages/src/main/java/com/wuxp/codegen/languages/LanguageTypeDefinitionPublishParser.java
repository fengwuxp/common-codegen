package com.wuxp.codegen.languages;

import com.wuxp.codegen.annotations.DefaultLanguageAnnotationParser;
import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.LanguageAnnotationParser;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.java.JavaBaseMeta;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class LanguageTypeDefinitionPublishParser<C extends CommonCodeGenClassMeta> implements LanguageTypeDefinitionParser<C>, LanguageAnnotationParser {


    private final List<LanguageElementDefinitionParser<? extends CommonBaseMeta, Object>> elementDefinitionParsers;

    private final List<LanguageDefinitionPostProcessor<? extends CommonBaseMeta>> postProcessors;

    private final List<CodeGenElementMatcher<?>> codeGenElementMatchers;

    private final LanguageAnnotationParser languageAnnotationParser;

    private final LanguageTypeDefinitionParser<C> mappingTypeDefinitionParser;

    public LanguageTypeDefinitionPublishParser(LanguageTypeDefinitionParser<C> mappingTypeDefinitionParser) {
        this.mappingTypeDefinitionParser = mappingTypeDefinitionParser;
        this.elementDefinitionParsers = new ArrayList<>();
        this.postProcessors = new ArrayList<>();
        this.codeGenElementMatchers = new ArrayList<>();
        this.languageAnnotationParser = new DefaultLanguageAnnotationParser(this.postProcessors);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public C parse(Class<?> source) {
        return publishParse(source);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C extends CommonBaseMeta> C publishParse(Object source) {
        if (!supports(source)) {
            return null;
        }
        C result = (C) mappingTypeDefinition(source);
        if (result != null) {
            return result;
        }
        if (matches(source)) {
            result = dispatchParse(source);
        }
        if (isParseCompleted(result)) {
            postProcess(result);
        }
        return result;
    }

    private boolean supports(Object source) {
        return source instanceof JavaBaseMeta || source instanceof AnnotatedElement;
    }

    private C mappingTypeDefinition(Object source) {
        if (source instanceof Class<?>) {
            return mappingTypeDefinitionParser.parse((Class<?>) source);
        }
        return null;
    }

    @Override
    public CommonCodeGenAnnotation[] parseAnnotatedElement(AnnotatedElement annotationOwner) {
        if (annotationOwner == null) {
            return new CommonCodeGenAnnotation[0];
        }
        return languageAnnotationParser.parseAnnotatedElement(annotationOwner);
    }

    @SuppressWarnings("unchecked")
    private boolean matches(Object source) {
        if (source == null) {
            return false;
        }
        boolean result = codeGenElementMatchers.stream()
                .filter(matcher -> matcher.supports(source.getClass()))
                .map(CodeGenElementMatcher.class::cast)
                .allMatch(matcher -> matcher.matches(source));
        if (!result) {
            log.warn("class={} not match", source.getClass().getName());
        }
        return result;
    }


    @SuppressWarnings({"unchecked"})
    private <C extends CommonBaseMeta> C dispatchParse(Object source) {
        for (LanguageElementDefinitionParser<? extends CommonBaseMeta, Object> parser : elementDefinitionParsers) {
            if (parser.supports(source.getClass())) {
                return (C) parser.parse(source);
            }
        }
        throw new CodegenRuntimeException(String.format("un support source，class name：%s", source.getClass().getName()));
    }

    private boolean isParseCompleted(CommonBaseMeta meta) {
        if (meta == null) {
            return false;
        }
        return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void postProcess(CommonBaseMeta meta) {
        for (LanguageDefinitionPostProcessor processor : postProcessors) {
            if (processor.supports(meta.getClass())) {
                processor.postProcess(meta);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void addElementDefinitionParser(LanguageElementDefinitionParser<? extends CommonBaseMeta, ?> parser) {
        this.elementDefinitionParsers.add((LanguageElementDefinitionParser<? extends CommonBaseMeta, Object>) parser);
    }

    public void addElementDefinitionParsers(Collection<LanguageElementDefinitionParser<? extends CommonBaseMeta, ?>> parsers) {
        parsers.forEach(this::addElementDefinitionParser);
    }

    public void addLanguageDefinitionPostProcessors(Collection<LanguageDefinitionPostProcessor<? extends CommonBaseMeta>> postProcessors) {
        this.postProcessors.addAll(postProcessors);
    }

    public void addCodeGenElementMatchers(Collection<CodeGenElementMatcher<?>> codeGenElementMatchers) {
        this.codeGenElementMatchers.addAll(codeGenElementMatchers);
    }

    public LanguageTypeDefinitionParser<C> getMappingTypeDefinitionParser() {
        return mappingTypeDefinitionParser;
    }
}
