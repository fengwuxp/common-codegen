package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.macth.DefaultCodeGenImportMatcher;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.parser.SimpleLanguageElementDefinitionDispatcher;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.MappingLanguageTypeDefinitionParser;
import com.wuxp.codegen.languages.MatchingLanguageElementDefinitionParser;
import com.wuxp.codegen.languages.examples.ExampleController;
import com.wuxp.codegen.mapping.TypescriptTypeMapping;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeScriptDefinitionParserTest {

    private LanguageTypeDefinitionParser<TypescriptClassMeta> definitionParser;

    @BeforeEach
    void setup() throws Exception {
        CodegenConfig codegenConfig = CodegenConfig.builder().providerType(ClientProviderType.TYPESCRIPT_FEIGN).build();
        CodegenConfigHolder.setConfig(codegenConfig);
        TypeScriptDefinitionParser typeScriptDefinitionParser = new TypeScriptDefinitionParser(getTestPackageMapStrategy());
        this.definitionParser = wrapperDefinitionParser(typeScriptDefinitionParser);
        SimpleLanguageElementDefinitionDispatcher dispatcher = SimpleLanguageElementDefinitionDispatcher.getInstance();
        dispatcher.addLanguageElementDefinitionParser(Class.class, this.definitionParser);
        dispatcher.addLanguageElementDefinitionParser(JavaMethodMeta.class, new TypeScriptMethodDefinitionParser(getTestPackageMapStrategy()));
    }

    private PackageMapStrategy getTestPackageMapStrategy() {
        return new PackageMapStrategy() {
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

    private LanguageTypeDefinitionParser<TypescriptClassMeta> wrapperDefinitionParser(LanguageTypeDefinitionParser<TypescriptClassMeta> delegate) {
        new MatchingLanguageElementDefinitionParser<TypescriptClassMeta>(delegate);
//        new MappingLanguageTypeDefinitionParser<>(delegate,new TypescriptTypeMapping());
        return null;
    }

    @Test
    void testParse() {
        TypescriptClassMeta result = definitionParser.parse(ExampleController.class);
        Assertions.assertNull(result);
    }
}