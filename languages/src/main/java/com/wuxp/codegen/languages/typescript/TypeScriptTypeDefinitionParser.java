package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.languages.AbstractLanguageTypeDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;

/**
 * @author wuxp
 */
public class TypeScriptTypeDefinitionParser extends AbstractLanguageTypeDefinitionParser<TypescriptClassMeta> {

    public TypeScriptTypeDefinitionParser(LanguageTypeDefinitionPublishParser<?> languageTypeDefinitionPublishParser,
                                          PackageNameConvertStrategy packageNameConvertStrategy) {
        super(languageTypeDefinitionPublishParser, packageNameConvertStrategy);
    }

    @Override
    public TypescriptClassMeta newElementInstance() {
        return new TypescriptClassMeta();
    }

}
