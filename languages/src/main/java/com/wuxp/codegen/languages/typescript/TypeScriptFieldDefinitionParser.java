package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.languages.AbstractLanguageFieldDefinitionParser;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;

public class TypeScriptFieldDefinitionParser extends AbstractLanguageFieldDefinitionParser<TypescriptFieldMate> {


    @Override
    public TypescriptFieldMate newElementInstance() {
        return new TypescriptFieldMate();
    }
}
