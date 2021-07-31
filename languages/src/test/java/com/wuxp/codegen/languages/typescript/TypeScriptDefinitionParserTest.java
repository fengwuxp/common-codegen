package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.macth.DispatchCodeGenElementMatcher;
import com.wuxp.codegen.core.macth.JavaClassElementMatcher;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.parser.SimpleLanguageElementDefinitionDispatcher;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.examples.ExampleController;
import com.wuxp.codegen.mapping.MappingTypescriptTypeDefinitionParser;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeScriptDefinitionParserTest {

    private LanguageTypeDefinitionParser<TypescriptClassMeta> languageTypeDefinitionParser;

    @BeforeEach
    void setup() throws Exception {
        CodegenConfig codegenConfig = CodegenConfig.builder().providerType(ClientProviderType.TYPESCRIPT_FEIGN).build();
        CodegenConfigHolder.setConfig(codegenConfig);
        TypeScriptTypeDefinitionParser typeScriptDefinitionParser = new TypeScriptTypeDefinitionParser(getTestPackageMapStrategy());
        this.languageTypeDefinitionParser = wrapperDefinitionParser(typeScriptDefinitionParser);
        configLanguageElementDefinitionDispatcher();
        DispatchCodeGenElementMatcher.getInstance().addCodeGenElementMatcher(Class.class, JavaClassElementMatcher.builder()
                .includePackages("com.wuxp.codegen.languages.examples")
                .build());

    }

    private void configLanguageElementDefinitionDispatcher() {
        SimpleLanguageElementDefinitionDispatcher languageElementDefinitionDispatcher = SimpleLanguageElementDefinitionDispatcher.getInstance();
        languageElementDefinitionDispatcher.addLanguageElementDefinitionParser(Class.class, this.languageTypeDefinitionParser);
        languageElementDefinitionDispatcher.addLanguageElementDefinitionParser(JavaMethodMeta.class, new TypeScriptMethodDefinitionParser(getTestPackageMapStrategy()));
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
        return MappingTypescriptTypeDefinitionParser.builder(delegate)
                .build();
    }

    @Test
    void testParse() {
        TypescriptClassMeta result = languageTypeDefinitionParser.parse(ExampleController.class);
        Assertions.assertNotNull(result);
    }
}