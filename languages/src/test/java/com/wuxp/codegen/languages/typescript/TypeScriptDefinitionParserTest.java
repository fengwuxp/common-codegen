package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.macth.JavaClassElementMatcher;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.languages.CommonMethodDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.languages.examples.ExampleController;
import com.wuxp.codegen.mapping.MappingTypescriptTypeDefinitionParser;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TypeScriptDefinitionParserTest {

    private LanguageTypeDefinitionParser<TypescriptClassMeta> languageTypeDefinitionParser;

    @BeforeEach
    void setup() throws Exception {
        CodegenConfig codegenConfig = CodegenConfig.builder().providerType(ClientProviderType.TYPESCRIPT_FEIGN).build();
        CodegenConfigHolder.setConfig(codegenConfig);
        this.languageTypeDefinitionParser = createDefinitionParser();

    }

    private PackageNameConvertStrategy getTestPackageMapStrategy() {
        return new PackageNameConvertStrategy() {
            @Override
            public String convert(Class<?> clazz) {
                return clazz.getName();
            }

            @Override
            public String convertClassName(Class<?> clazz) {
                return clazz.getName();
            }

            @Override
            public String genPackagePath(String[] uris) {
                return "";
            }
        };
    }

    private LanguageTypeDefinitionPublishParser<TypescriptClassMeta> createDefinitionParser() {
        LanguageTypeDefinitionPublishParser<TypescriptClassMeta> result = new LanguageTypeDefinitionPublishParser<>(MappingTypescriptTypeDefinitionParser.builder()
                .build());
        result.addElementDefinitionParsers(getElementDefinitionParsers(result));
        result.addCodeGenElementMatchers(Collections.singletonList(JavaClassElementMatcher.builder().build()));
        return result;
    }

    private List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ? extends Object>> getElementDefinitionParsers(LanguageTypeDefinitionPublishParser<TypescriptClassMeta> result) {
        TypeScriptTypeDefinitionParser typeScriptDefinitionParser = new TypeScriptTypeDefinitionParser(result, getTestPackageMapStrategy());
        return Arrays.asList(typeScriptDefinitionParser,
                getTypeScriptMethodDefinitionParser(result),
                new TypeScriptFieldDefinitionParser(result),
                new TypeScriptTypeVariableDefinitionParser()
        );
    }

    private CommonMethodDefinitionParser getTypeScriptMethodDefinitionParser(LanguageTypeDefinitionPublishParser<TypescriptClassMeta> publishParser) {
        return new CommonMethodDefinitionParser(publishParser, getTestPackageMapStrategy());
    }

    @Test
    void testParse() {
        TypescriptClassMeta result = languageTypeDefinitionParser.parse(ExampleController.class);
        Assertions.assertNotNull(result);
    }
}