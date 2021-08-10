package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;

/**
 * @author wuxp
 */
public interface LanguageTypeDefinitionParser<C extends CommonCodeGenClassMeta> extends LanguageElementDefinitionParser<C, Class<?>> {

    @Override
    default boolean supports(Class<?> source) {
        return source == Class.class;
    }
}

