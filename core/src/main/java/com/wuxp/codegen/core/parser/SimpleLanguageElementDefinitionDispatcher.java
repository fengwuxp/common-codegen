package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.model.CommonBaseMeta;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wuxp
 */
public class SimpleLanguageElementDefinitionDispatcher implements LanguageElementDefinitionDispatcher {

    private static final SimpleLanguageElementDefinitionDispatcher INSTANCE = new SimpleLanguageElementDefinitionDispatcher();

    @SuppressWarnings("rawtypes")
    private final Map<Class<?>, LanguageElementDefinitionParser> parserCaches;

    private SimpleLanguageElementDefinitionDispatcher() {
        this.parserCaches = new HashMap<>();
    }

    public static SimpleLanguageElementDefinitionDispatcher getInstance() {
        return INSTANCE;
    }

    @Override
    public <T extends CommonBaseMeta> Optional<T> dispatchOfNullable(Object value) {
        LanguageElementDefinitionParser<T, Object> elementDefinitionParser = getDefinitionParser(value);
        return elementDefinitionParser.parseOfNullable(value);
    }

    @Nullable
    @Override
    public <T extends CommonBaseMeta> T dispatch(Object value) {
        LanguageElementDefinitionParser<T, Object> elementDefinitionParser = getDefinitionParser(value);
        return elementDefinitionParser.parse(value);
    }

    @SuppressWarnings("rawtypes")
    public void addLanguageElementDefinitionParser(Class<?> clazz, LanguageElementDefinitionParser languageEnhancedProcessor) {
        parserCaches.put(clazz, languageEnhancedProcessor);
    }

    @SuppressWarnings("unchecked")
    private <T extends CommonBaseMeta> LanguageElementDefinitionParser<T, Object> getDefinitionParser(Object value) {
        LanguageElementDefinitionParser<T, Object> result = parserCaches.get(value.getClass());
        if (result == null) {
            throw new CodegenRuntimeException(String.format("未找到 value Class =%s 的 LanguageElementDefinitionParser", value.getClass().getName()));
        }
        return result;
    }
}
