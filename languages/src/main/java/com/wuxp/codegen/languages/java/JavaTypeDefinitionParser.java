package com.wuxp.codegen.languages.java;

import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.languages.AbstractLanguageTypeDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;

public class JavaTypeDefinitionParser extends AbstractLanguageTypeDefinitionParser<JavaCodeGenClassMeta> {

    public JavaTypeDefinitionParser(LanguageTypeDefinitionPublishParser<?> languageTypeDefinitionPublishParser, PackageNameConvertStrategy packageNameConvertStrategy) {
        super(languageTypeDefinitionPublishParser, packageNameConvertStrategy);
    }

    @Override
    public JavaCodeGenClassMeta newElementInstance() {
        return new JavaCodeGenClassMeta();
    }
}
