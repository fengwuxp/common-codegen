package com.wuxp.codegen.languages.dart;

import com.wuxp.codegen.core.parser.LanguageTypeVariableDefinitionParser;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;

public class DartTypeVariableDefinitionParser implements LanguageTypeVariableDefinitionParser<DartClassMeta> {

    @Override
    public DartClassMeta newElementInstance() {
        return new DartClassMeta();
    }

    @Override
    public DartClassMeta parse(TypeVariable<? extends GenericDeclaration> source) {
        DartClassMeta result = newElementInstance();
        BeanUtils.copyProperties(DartClassMeta.TYPE_VARIABLE, result);
        result.setName(source.getName());
        result.setGenericDescription(source.getName());
        return result;
    }
}
