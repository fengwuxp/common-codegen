package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractLanguageMethodDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;

/**
 * @author wuxp
 */
public class TypeScriptMethodDefinitionParser extends AbstractLanguageMethodDefinitionParser<CommonCodeGenMethodMeta> {

    public TypeScriptMethodDefinitionParser(PackageMapStrategy packageMapStrategy) {
        super(packageMapStrategy);
    }

    @Override
    public CommonCodeGenClassMeta newTypeVariableInstance() {
        return null;
    }
}
