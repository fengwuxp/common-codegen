package com.wuxp.codegen.languages;

import com.wuxp.codegen.annotations.DefaultLanguageAnnotationParser;
import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.LanguageAnnotationParser;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.core.util.MatchCodeGenClassSupportHandlerUtils;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
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

    public LanguageTypeDefinitionPublishParser() {
        this.elementDefinitionParsers = new ArrayList<>();
        this.postProcessors = new ArrayList<>();
        this.codeGenElementMatchers = new ArrayList<>();
        this.languageAnnotationParser = new DefaultLanguageAnnotationParser(this.postProcessors);
    }

    @Override
    public boolean supports(Class<?> source) {
        return true;
    }

    @Override
    public C parse(Class<?> source) {
        return publishParse(source);
    }

    @Override
    public <C extends CommonBaseMeta> C publishParse(Object source) {
        if (!matches(source)) {
            return null;
        }
        C result = dispatchParse(source);
        if (result != null) {
            postProcess(result);
        }
        return result;
    }

    @Override
    public CommonCodeGenAnnotation[] parseAnnotatedElement(AnnotatedElement annotationOwner) {
        if (annotationOwner == null) {
            return new CommonCodeGenAnnotation[0];
        }
        return languageAnnotationParser.parseAnnotatedElement(annotationOwner);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean matches(Object source) {
        if (source == null) {
            return false;
        }
        CodeGenElementMatcher codeGenElementMatcher = MatchCodeGenClassSupportHandlerUtils.matchesHandler(codeGenElementMatchers, source.getClass());
        if (codeGenElementMatcher == null) {
            log.warn("not found source: {} matcher", source.getClass().getName());
            return true;
        }
        return codeGenElementMatcher.matches(source);
    }

    @SuppressWarnings({"unchecked"})
    private <C extends CommonBaseMeta> C dispatchParse(Object source) {
        return (C) getLanguageElementDefinitionParser(source.getClass()).parse(source);
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
    private <C extends CommonBaseMeta> LanguageElementDefinitionParser<C, Object> getLanguageElementDefinitionParser(Class<?> sourceClass) {
        LanguageElementDefinitionParser<? extends CommonBaseMeta, Object> elementDefinitionParser = MatchCodeGenClassSupportHandlerUtils.matchesHandler(elementDefinitionParsers, sourceClass);
        if (elementDefinitionParser == null) {
            throw new CodegenRuntimeException(String.format("un support source，class name：%s", sourceClass.getName()));
        }
        return (LanguageElementDefinitionParser<C, Object>) elementDefinitionParser;
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


}
