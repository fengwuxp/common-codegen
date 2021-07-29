package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;


/**
 * @author wuxp
 */
public abstract class DelegateLanguageTypeDefinitionParser <C extends CommonCodeGenClassMeta> implements LanguageTypeDefinitionParser<C> {

    private final LanguageTypeDefinitionParser<C> delegate;

    protected DelegateLanguageTypeDefinitionParser(LanguageTypeDefinitionParser<C> delegate) {
        this.delegate = delegate;
    }

    @Override
    public C newInstance() {
        return delegate.newInstance();
    }

    @Override
    public CommonCodeGenClassMeta newTypeVariableInstance() {
        return delegate.newTypeVariableInstance();
    }

    public LanguageTypeDefinitionParser<C> getDelegate() {
        return delegate;
    }
}
