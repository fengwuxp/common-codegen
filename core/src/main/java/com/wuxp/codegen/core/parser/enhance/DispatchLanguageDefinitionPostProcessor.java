package com.wuxp.codegen.core.parser.enhance;

import com.wuxp.codegen.model.CommonBaseMeta;

import java.util.*;

/**
 * @author wuxp
 */
public final class DispatchLanguageDefinitionPostProcessor<C extends CommonBaseMeta> implements LanguageDefinitionPostProcessor<C> {

    private static final DispatchLanguageDefinitionPostProcessor<? extends CommonBaseMeta> INSTANCE = new DispatchLanguageDefinitionPostProcessor<>();

    private final Map<Class<C>, List<LanguageDefinitionPostProcessor<C>>> processors;

    private DispatchLanguageDefinitionPostProcessor() {
        this.processors = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public static <T extends CommonBaseMeta> DispatchLanguageDefinitionPostProcessor<T> getInstance() {
        return (DispatchLanguageDefinitionPostProcessor<T>) INSTANCE;
    }

    @Override
    public C postProcess(C meta) {
        return getLanguageDefinitionPostProcessor(meta).postProcess(meta);
    }

    public void addLanguageDefinitionPostProcessor(Class<C> clazz, LanguageDefinitionPostProcessor<C> postProcessor) {
        this.processors.computeIfAbsent(clazz, key -> new ArrayList<>()).add(postProcessor);
    }

    private LanguageDefinitionPostProcessor<C> getLanguageDefinitionPostProcessor(C meta) {
        return combinePostProcessor(this.processors.getOrDefault(meta.getClass(), Collections.emptyList()));
    }

    private LanguageDefinitionPostProcessor<C> combinePostProcessor(List<LanguageDefinitionPostProcessor<C>> processors) {
        return meta -> {
            for (LanguageDefinitionPostProcessor<C> processor : processors) {
                meta = processor.postProcess(meta);
            }
            return meta;
        };
    }
}
