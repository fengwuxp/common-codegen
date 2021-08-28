package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class AbstractMappingTypeDefinitionParserBuilder<C extends CommonCodeGenClassMeta> {

    /**
     * 类型映射器
     */
    private final Map<Class<?>, C> typeMappings;


    AbstractMappingTypeDefinitionParserBuilder(Map<Class<?>, C> baseMappings) {
        this.typeMappings = new HashMap<>(baseMappings);
    }

    public AbstractMappingTypeDefinitionParserBuilder<C> typeMapping(Class<?> clazz, C meta) {
        this.typeMappings.put(clazz, meta);
        return this;
    }

    @SuppressWarnings("unchecked")
    public AbstractMappingTypeDefinitionParserBuilder<C> typeMapping(Map<Class<?>, ? extends CommonCodeGenClassMeta> typeMappings) {
        this.typeMappings.putAll((Map<Class<?>, ? extends C>) typeMappings);
        return this;
    }

    public abstract LanguageTypeDefinitionParser<C> build();

}
