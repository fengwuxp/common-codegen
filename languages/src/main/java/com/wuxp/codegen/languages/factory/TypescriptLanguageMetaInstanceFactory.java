package com.wuxp.codegen.languages.factory;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TypescriptLanguageMetaInstanceFactory implements
        LanguageParser.LanguageMetaInstanceFactory<TypescriptClassMeta, CommonCodeGenMethodMeta, TypescriptFieldMate> {

    @Override
    public TypescriptClassMeta newClassInstance() {
        return new TypescriptClassMeta();
    }

    @Override
    public TypescriptFieldMate newFieldInstance() {
        return new TypescriptFieldMate();
    }

    @Override
    public TypescriptClassMeta getTypeVariableInstance() {
        return TypescriptClassMeta.TYPE_VARIABLE;
    }
}
