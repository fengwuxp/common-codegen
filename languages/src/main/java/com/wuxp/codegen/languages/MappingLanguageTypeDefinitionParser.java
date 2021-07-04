package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.mapping.AbstractLanguageTypeMapping;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author wuxp
 */
public class MappingLanguageTypeDefinitionParser<C extends CommonCodeGenClassMeta> implements LanguageTypeDefinitionParser<C> {

    private final LanguageTypeDefinitionParser<C> delegate;

    /**
     * 映射java类型和其他语言类型之间的关系
     */
    private final AbstractLanguageTypeMapping<C> languageTypeMapping;

    public MappingLanguageTypeDefinitionParser(LanguageTypeDefinitionParser<C> delegate, AbstractLanguageTypeMapping<C> languageTypeMapping) {
        this.delegate = delegate;
        this.languageTypeMapping = languageTypeMapping;
    }

    @Override
    public C parse(Class<?> source) {
        List<C> results = this.languageTypeMapping.mapping(source);
        if (!results.isEmpty()) {
            return results.get(0);
        }

        C result = languageTypeMapping.getCombineTypeMapping().mapping(source);
        if (result != null) {
            return result;
        }
        return delegate.parse(source);
    }

    @Override
    public C newInstance() {
        return delegate.newInstance();
    }

    @Override
    public <M extends CommonCodeGenClassMeta> M newTypeVariableInstance() {
        return delegate.newTypeVariableInstance();
    }

    @Override
    public <M extends CommonCodeGenClassMeta> M parseTypeVariable(Type type) {
        return delegate.parseTypeVariable(type);
    }
}
