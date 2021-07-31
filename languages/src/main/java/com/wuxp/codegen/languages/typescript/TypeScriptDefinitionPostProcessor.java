package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;

/**
 * @author wuxp
 */
public class TypeScriptDefinitionPostProcessor implements LanguageDefinitionPostProcessor<TypescriptClassMeta> {

    @Override
    public TypescriptClassMeta postProcess(TypescriptClassMeta meta) {
        return meta;
    }
}
