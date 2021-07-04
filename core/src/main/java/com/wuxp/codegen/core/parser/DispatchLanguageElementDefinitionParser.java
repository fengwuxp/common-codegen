package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonBaseMeta;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wuxp
 */
public class DispatchLanguageElementDefinitionParser {

    private static final DispatchLanguageElementDefinitionParser INSTANCE = new DispatchLanguageElementDefinitionParser();

    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, LanguageElementDefinitionParser> parsers;

    private DispatchLanguageElementDefinitionParser() {
        this.parsers = new HashMap<>();
    }

    public static DispatchLanguageElementDefinitionParser getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <T extends CommonBaseMeta> Optional<T> dispatchOfNullable(Object value) {
        return parsers.get(value.getClass()).parseOfNullable(value);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T extends CommonBaseMeta> T dispatch(Object value) {
        return (T) parsers.get(value.getClass()).parse(value);
    }

    @SuppressWarnings("rawtypes")
    public void addLanguageElementDefinitionParser(Class<?> clazz, LanguageElementDefinitionParser languageEnhancedProcessor) {
        parsers.put(clazz, languageEnhancedProcessor);
    }
}
