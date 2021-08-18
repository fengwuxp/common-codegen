package com.wuxp.codegen.languages;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;

public class CommonFieldDefinitionParser extends AbstractLanguageFieldDefinitionParser<CommonCodeGenFiledMeta> {

    public CommonFieldDefinitionParser(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishSourceParser) {
        super(publishSourceParser);
    }
}
