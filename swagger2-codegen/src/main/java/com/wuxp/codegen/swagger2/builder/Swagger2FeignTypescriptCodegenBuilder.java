package com.wuxp.codegen.swagger2.builder;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.macth.ExcludeAnnotationCodeGenElementMatcher;
import com.wuxp.codegen.core.macth.JavaClassElementMatcher;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.languages.*;
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
import com.wuxp.codegen.swagger2.annotations.*;
import io.swagger.annotations.*;
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
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(Api.class, new ApiMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiModel.class, new ApiModelMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiModelProperty.class, new ApiModelPropertyMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiOperation.class, new ApiOperationMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiParam.class, new ApiParamMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiImplicitParam.class, new ApiImplicitParamMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiImplicitParams.class, new ApiImplicitParamsMetaFactory());

        this.elementParsePostProcessors(new RemoveClientResponseTypePostProcessor(TypescriptClassMeta.PROMISE), new EnumDefinitionPostProcessor());
        this.codeGenElementMatchers(
                new ExcludeAnnotationCodeGenElementMatcher(Collections.singletonList(ApiIgnore.class)),
                JavaClassElementMatcher.builder()
                        .includePackages(this.getIncludePackages())
                        .includeClasses(this.getIncludeClasses())
                        .includePackages(this.getIgnorePackages())
                        .ignoreClasses(this.getIgnoreClasses())
                        .build()
        );

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
                this.getOutPath(),
                this.getLanguageDescription().getSuffixName(),
                this.getIsDeletedOutputDirectory(),
                this.getCodeFormatter());
    }

    private LanguageTypeDefinitionParser<TypescriptClassMeta> configureAndGetDefinitionParser() {
        LanguageTypeDefinitionPublishParser<TypescriptClassMeta> result = new LanguageTypeDefinitionPublishParser<>(getMappingTypescriptTypeDefinitionParser());
        result.addElementDefinitionParsers(getElementDefinitionParsers(result));
        result.addCodeGenElementMatchers(this.getCodeGenElementMatchers());
        result.addLanguageDefinitionPostProcessors(this.getElementParsePostProcessors());
        return result;
    }

    private List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ? extends Object>> getElementDefinitionParsers(LanguageTypeDefinitionPublishParser<TypescriptClassMeta> result) {
        TypeScriptTypeDefinitionParser typeScriptDefinitionParser = new TypeScriptTypeDefinitionParser(result, this.packageMapStrategy);
        List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ?>> parsers = Arrays.asList(
                typeScriptDefinitionParser,
                getTypeScriptMethodDefinitionParser(result),
                new TypeScriptFieldDefinitionParser(result),
                new TypeScriptTypeVariableDefinitionParser()
        );
        configureElementParsers(parsers);
        return parsers;
    }

    private void configureElementParsers(List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ? extends Object>> elementDefinitionParsers) {
        JavaTypeMapper javaTypeMapper = new JavaTypeMapper(customJavaTypeMapping);
        elementDefinitionParsers.forEach(languageElementDefinitionParser -> {
            if (languageElementDefinitionParser instanceof DelegateLanguagePublishParser) {
                ((DelegateLanguagePublishParser) languageElementDefinitionParser).setJavaTypeMapper(javaTypeMapper);
            }
        });
    }

    private LanguageTypeDefinitionParser<TypescriptClassMeta> getMappingTypescriptTypeDefinitionParser() {
        return MappingTypescriptTypeDefinitionParser.builder()
                .typeMapping(baseTypeMapping)
                .build();
    }


    private TypeScriptMethodDefinitionParser getTypeScriptMethodDefinitionParser(LanguageTypeDefinitionPublishParser<TypescriptClassMeta> publishParser) {
        return new TypeScriptMethodDefinitionParser(publishParser, this.packageMapStrategy);
    }
}
