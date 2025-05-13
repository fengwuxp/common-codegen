package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.Getter;


/**
 * 将类型解析委托给 {@link #delegate}
 *
 * @author wuxp
 */
@Getter
public abstract class DelegateLanguageTypeDefinitionParser<C extends CommonCodeGenClassMeta> implements LanguageTypeDefinitionParser<C> {

    private final LanguageTypeDefinitionParser<C> delegate;

    protected DelegateLanguageTypeDefinitionParser(LanguageTypeDefinitionParser<C> delegate) {
        this.delegate = delegate;
    }

    @Override
    public C newElementInstance() {
        return getDelegate().newElementInstance();
    }

    @Override
    public <C extends CommonBaseMeta> C publishParse(Object source) {
        return getDelegate().publishParse(source);
    }

    @Override
    public boolean supports(Class<?> source) {
        return getDelegate().supports(source);
    }
}
