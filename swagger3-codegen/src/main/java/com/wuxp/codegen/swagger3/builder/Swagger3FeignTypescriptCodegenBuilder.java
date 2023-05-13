package com.wuxp.codegen.swagger3.builder;

import com.google.common.collect.ImmutableSet;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.languages.CommonMethodDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.languages.typescript.*;
import com.wuxp.codegen.mapping.MappingTypescriptTypeDefinitionParser;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author wuxp
 */
@Slf4j
public class Swagger3FeignTypescriptCodegenBuilder extends AbstractSwagger3CodegenBuilder {


    protected Swagger3FeignTypescriptCodegenBuilder() {
        super();
    }


    public static Swagger3FeignTypescriptCodegenBuilder builder() {
        return new Swagger3FeignTypescriptCodegenBuilder();
    }

    @Override
    public CodeGenerator buildCodeGenerator() {
        initCodegenConfig(LanguageDescription.TYPESCRIPT, ClientProviderType.TYPESCRIPT_FEIGN);
        if (ImmutableSet.of(ClientProviderType.UMI_REQUEST, ClientProviderType.AXIOS).contains(this.clientProviderType)) {
            this.elementParsePostProcessors(new UmiRequestMethodDefinitionPostProcessor());
        }
        configParserPostProcessors(TypescriptClassMeta.PROMISE);
        configCodeGenElementMatchers();
        configUmiModel();
        return createCodeGenerator();
    }

    private void configUmiModel() {
        boolean needSetUmiModel = !this.getSharedVariables().containsKey("umiModel") && ClientProviderType.UMI_REQUEST.equals(this.clientProviderType);
        if (needSetUmiModel) {
            this.getSharedVariables().put("umiModel", UmiModel.OPEN_SOURCE);
        }
    }

    @Override
    protected List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ? extends Object>> getElementDefinitionParsers(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishParser) {
        CommonMethodDefinitionParser methodDefinitionParser = getLanguageMethodDefinitionParser(publishParser);
        methodDefinitionParser.setMargeMethodParams(true);
        List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ?>> parsers = Arrays.asList(
                new TypeScriptTypeDefinitionParser(publishParser, this.getPackageMapStrategy()),
                methodDefinitionParser,
                new TypeScriptFieldDefinitionParser(publishParser),
                new TypeScriptTypeVariableDefinitionParser()
        );
        configureElementParsers(parsers);
        return parsers;
    }

    @Override
    protected LanguageTypeDefinitionParser<TypescriptClassMeta> getMappingTypeDefinitionParser() {
        return MappingTypescriptTypeDefinitionParser.builder()
                .typeMapping(baseTypeMapping)
                .build();
    }
}
