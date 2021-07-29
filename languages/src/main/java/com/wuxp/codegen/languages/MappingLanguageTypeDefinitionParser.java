package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.mapping.AbstractLanguageTypeMapping;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.List;

/**
 * @author wuxp
 */
public class MappingLanguageTypeDefinitionParser<C extends CommonCodeGenClassMeta> extends DelegateLanguageTypeDefinitionParser<C> {

    /**
     * 映射java类型和其他语言类型之间的关系
     */
    private final AbstractLanguageTypeMapping<C> languageTypeMapping;

    public MappingLanguageTypeDefinitionParser(LanguageTypeDefinitionParser<C> delegate, AbstractLanguageTypeMapping<C> languageTypeMapping) {
        super(delegate);
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
        return getDelegate().parse(source);
    }

}
