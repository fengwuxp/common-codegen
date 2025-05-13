package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;

/**
 * 解析不同语言的类类型
 * @author wuxp
 */
public interface LanguageTypeDefinitionParser<C extends CommonCodeGenClassMeta> extends LanguageElementDefinitionParser<C, Class<?>> {

    @Override
    default boolean supports(Class<?> source) {
        return source == Class.class;
    }
}

