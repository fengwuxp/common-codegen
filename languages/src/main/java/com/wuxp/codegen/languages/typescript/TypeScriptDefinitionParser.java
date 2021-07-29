package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractLanguageTypeDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import org.springframework.beans.BeanUtils;

/**
 * @author wuxp
 */
public class TypeScriptDefinitionParser extends AbstractLanguageTypeDefinitionParser<TypescriptClassMeta> {

    public TypeScriptDefinitionParser(PackageMapStrategy packageMapStrategy) {
        super(packageMapStrategy);
    }

    @Override
    public TypescriptClassMeta newInstance() {
        return new TypescriptClassMeta();
    }

    @Override
    public CommonCodeGenClassMeta newTypeVariableInstance() {
        TypescriptClassMeta typescriptClassMeta = newInstance();
        BeanUtils.copyProperties(CommonCodeGenClassMeta.TYPE_VARIABLE, typescriptClassMeta);
        typescriptClassMeta.setSuperClass(TypescriptClassMeta.OBJECT)
                .setNeedGenerate(false)
                .setNeedImport(false);
        return typescriptClassMeta;
    }
}
