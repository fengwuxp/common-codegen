package com.wuxp.codegen.languages.java;

import com.wuxp.codegen.core.parser.LanguageTypeVariableDefinitionParser;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;

public class JavaTypeVariableDefinitionParser implements LanguageTypeVariableDefinitionParser<JavaCodeGenClassMeta> {

    @Override
    public JavaCodeGenClassMeta newElementInstance() {
        return new JavaCodeGenClassMeta();
    }

    @Override
    public JavaCodeGenClassMeta parse(TypeVariable<? extends GenericDeclaration> source) {
        JavaCodeGenClassMeta result = newElementInstance();
        BeanUtils.copyProperties(JavaCodeGenClassMeta.TYPE_VARIABLE, result);
        result.setName(source.getName());
        result.setGenericDescription(source.getName());
        return result;
    }
}
