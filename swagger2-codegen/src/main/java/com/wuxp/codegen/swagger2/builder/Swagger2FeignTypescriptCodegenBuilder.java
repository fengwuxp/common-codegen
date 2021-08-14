package com.wuxp.codegen.swagger2.builder;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.macth.ExcludeAnnotationCodeGenElementMatcher;
import com.wuxp.codegen.core.macth.JavaClassElementMatcher;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.languages.RemoveClientResponseTypePostProcessor;
import com.wuxp.codegen.languages.typescript.TypeScriptFieldDefinitionParser;
import com.wuxp.codegen.languages.typescript.TypeScriptMethodDefinitionParser;
import com.wuxp.codegen.languages.typescript.TypeScriptTypeDefinitionParser;
import com.wuxp.codegen.languages.typescript.TypeScriptTypeVariableDefinitionParser;
import com.wuxp.codegen.loong.LoongDefaultCodeGenerator;
import com.wuxp.codegen.loong.LoongSimpleTemplateStrategy;
import com.wuxp.codegen.mapping.MappingTypescriptTypeDefinitionParser;
import com.wuxp.codegen.meta.enums.EnumDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wuxp
 */
@Slf4j
public class Swagger2FeignTypescriptCodegenBuilder extends AbstractLoongCodegenBuilder {


    private Swagger2FeignTypescriptCodegenBuilder() {
        super();
    }


    public static Swagger2FeignTypescriptCodegenBuilder builder() {
        return new Swagger2FeignTypescriptCodegenBuilder();
    }

    @Override
    public CodeGenerator buildCodeGenerator() {
        if (this.languageDescription == null) {
            this.languageDescription = LanguageDescription.TYPESCRIPT;
        }
        if (this.clientProviderType == null) {
            this.clientProviderType = ClientProviderType.TYPESCRIPT_FEIGN;
        }
        this.initTypeMapping();
        if (ClientProviderType.UMI_REQUEST.equals(this.clientProviderType)) {
//            DispatchLanguageDefinitionPostProcessor.getInstance().addLanguageDefinitionPostProcessor(new UmiRequestMethodDefinitionPostProcessor());
        }
        LoongDefaultCodeGenerator codeGenerator = new LoongDefaultCodeGenerator(scanPackages, configureAndGetDefinitionParser(), getTemplateStrategy());
        codeGenerator.setIgnoreClasses(ignoreClasses);
        codeGenerator.setIncludeClasses(includeClasses);
        codeGenerator.setIgnorePackages(ignorePackages);
        codeGenerator.setEnableFieldUnderlineStyle(enableFieldUnderlineStyle);
        return codeGenerator;
    }

    private TemplateStrategy<CommonCodeGenClassMeta> getTemplateStrategy() {
        return new LoongSimpleTemplateStrategy(
                this.getTemplateLoader(),
                this.outPath,
                this.languageDescription.getSuffixName(),
                this.isDeletedOutputDirectory, this.codeFormatter);
    }

    private LanguageTypeDefinitionParser<TypescriptClassMeta> configureAndGetDefinitionParser() {
        LanguageTypeDefinitionPublishParser<TypescriptClassMeta> result = new LanguageTypeDefinitionPublishParser<>(getMappingTypescriptTypeDefinitionParser());
        result.addElementDefinitionParsers(getElementDefinitionParsers(result));
        result.addCodeGenElementMatchers(Arrays.asList(
                new ExcludeAnnotationCodeGenElementMatcher(Collections.singletonList(ApiIgnore.class)),
                JavaClassElementMatcher.builder().build()
        ));
        result.addLanguageDefinitionPostProcessors(Arrays.asList(
                new RemoveClientResponseTypePostProcessor(TypescriptClassMeta.PROMISE),
                new EnumDefinitionPostProcessor()
        ));
        return result;
    }

    private List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ? extends Object>> getElementDefinitionParsers(LanguageTypeDefinitionPublishParser<TypescriptClassMeta> result) {
        TypeScriptTypeDefinitionParser typeScriptDefinitionParser = new TypeScriptTypeDefinitionParser(result, this.packageMapStrategy);
        return Arrays.asList(
                typeScriptDefinitionParser,
                getTypeScriptMethodDefinitionParser(result),
                new TypeScriptFieldDefinitionParser(result),
                new TypeScriptTypeVariableDefinitionParser()
        );
    }

    private LanguageTypeDefinitionParser<TypescriptClassMeta> getMappingTypescriptTypeDefinitionParser() {
        return MappingTypescriptTypeDefinitionParser.builder()
                .javaTypeMappings(customJavaTypeMapping)
                .typeMapping(baseTypeMapping)
                .build();
    }


    private TypeScriptMethodDefinitionParser getTypeScriptMethodDefinitionParser(LanguageTypeDefinitionPublishParser<TypescriptClassMeta> publishParser) {
        return new TypeScriptMethodDefinitionParser(publishParser, this.packageMapStrategy);
    }
}
