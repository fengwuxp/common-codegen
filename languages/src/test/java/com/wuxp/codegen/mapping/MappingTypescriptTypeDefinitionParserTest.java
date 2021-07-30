package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MappingTypescriptTypeDefinitionParserTest {


    private LanguageTypeDefinitionParser<TypescriptClassMeta> wrapperDefinitionParser(LanguageTypeDefinitionParser<TypescriptClassMeta> delegate) {
        return MappingTypescriptTypeDefinitionParser.builder(delegate)
                .build();
    }

    @Test
    void testMapping() {
        LanguageTypeDefinitionParser<TypescriptClassMeta> definitionParser = wrapperDefinitionParser(new LanguageTypeDefinitionParser<TypescriptClassMeta>() {
            @Override
            public TypescriptClassMeta newElementInstance() {
                return null;
            }

            @Override
            public CommonCodeGenClassMeta newTypeVariableInstance() {
                return null;
            }

            @Override
            public TypescriptClassMeta parse(Class<?> source) {
                return null;
            }
        });
        TypescriptClassMeta result = definitionParser.parse(String.class);
        Assertions.assertNotNull(result, "String type mapping result must not null");
    }
}