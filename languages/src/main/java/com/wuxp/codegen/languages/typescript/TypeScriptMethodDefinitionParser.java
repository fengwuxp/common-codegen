package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.languages.AbstractLanguageMethodDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;

/**
 * @author wuxp
 */
public class TypeScriptMethodDefinitionParser extends AbstractLanguageMethodDefinitionParser<CommonCodeGenMethodMeta> {

    public TypeScriptMethodDefinitionParser(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> languageElementDefinitionEventParser,
                                            PackageNameConvertStrategy packageMapStrategy) {
        super(languageElementDefinitionEventParser, packageMapStrategy);
    }

    @Override
    protected boolean needMargeMethodParams() {
        return true;
    }
}
