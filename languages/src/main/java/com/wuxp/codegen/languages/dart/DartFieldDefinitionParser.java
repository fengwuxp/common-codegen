package com.wuxp.codegen.languages.dart;

import com.wuxp.codegen.languages.AbstractLanguageFieldDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.dart.DartFieldMate;

public class DartFieldDefinitionParser extends AbstractLanguageFieldDefinitionParser<DartFieldMate> {

    public DartFieldDefinitionParser(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishSourceParser) {
        super(publishSourceParser);
    }

    @Override
    public DartFieldMate newElementInstance() {
        return new DartFieldMate();
    }
}
