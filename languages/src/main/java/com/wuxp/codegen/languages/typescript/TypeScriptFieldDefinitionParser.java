package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.languages.AbstractLanguageFieldDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;

public class TypeScriptFieldDefinitionParser extends AbstractLanguageFieldDefinitionParser<TypescriptFieldMate> {

    public TypeScriptFieldDefinitionParser(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishSourceParser) {
        super(publishSourceParser);
    }

    @Override
    public TypescriptFieldMate newElementInstance() {
        return new TypescriptFieldMate();
    }
}
