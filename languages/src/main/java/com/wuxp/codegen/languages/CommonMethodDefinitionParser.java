package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;

/**
 * @author wuxp
 */
public class CommonMethodDefinitionParser extends AbstractLanguageMethodDefinitionParser<CommonCodeGenMethodMeta> {

    public CommonMethodDefinitionParser(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> languageElementDefinitionEventParser,
                                        PackageNameConvertStrategy packageMapStrategy) {
        super(languageElementDefinitionEventParser, packageMapStrategy);
    }

    @Override
    protected boolean needMargeMethodParams() {
        return true;
    }
}
