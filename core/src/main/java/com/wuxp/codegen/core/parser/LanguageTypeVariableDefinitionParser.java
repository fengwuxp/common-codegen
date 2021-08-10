package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;

public interface LanguageTypeVariableDefinitionParser<C extends CommonCodeGenClassMeta> extends LanguageElementDefinitionParser<C, TypeVariable<? extends GenericDeclaration>> {

    @Override
    default boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, TypeVariable.class);
    }

}
