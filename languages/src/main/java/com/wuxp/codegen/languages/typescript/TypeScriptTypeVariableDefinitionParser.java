package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.parser.LanguageTypeVariableDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;

public class TypeScriptTypeVariableDefinitionParser implements LanguageTypeVariableDefinitionParser<TypescriptClassMeta> {

    @Override
    public TypescriptClassMeta newElementInstance() {
        return new TypescriptClassMeta();
    }

    @Override
    public TypescriptClassMeta parse(TypeVariable<? extends GenericDeclaration> source) {
        TypescriptClassMeta result = newElementInstance();
        BeanUtils.copyProperties(CommonCodeGenClassMeta.TYPE_VARIABLE, result);
        result.setSuperClass(TypescriptClassMeta.OBJECT)
                .setNeedGenerate(false)
                .setNeedImport(false)
                .setTypeArgumentVariable(true);
        return result;
    }
}
