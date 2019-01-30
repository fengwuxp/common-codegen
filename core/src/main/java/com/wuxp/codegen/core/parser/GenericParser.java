package com.wuxp.codegen.core.parser;

/**
 * generic parser
 */
public interface GenericParser<T,F> {

    /**
     * parse file
     * @param source  a source of type `T`
     * @return parse result
     */
    T parse(F source);
}

