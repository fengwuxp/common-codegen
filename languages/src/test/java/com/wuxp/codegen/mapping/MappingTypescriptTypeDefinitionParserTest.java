package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MappingTypescriptTypeDefinitionParserTest {


    @Test
    void testMapping() {
        LanguageTypeDefinitionParser<TypescriptClassMeta> definitionParser = MappingTypescriptTypeDefinitionParser.builder()
                .build();
        TypescriptClassMeta result = definitionParser.parse(String.class);
        Assertions.assertNotNull(result, "String type mapping result must not null");
    }
}