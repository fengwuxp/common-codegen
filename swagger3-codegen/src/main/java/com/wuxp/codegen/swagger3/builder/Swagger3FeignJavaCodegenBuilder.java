package com.wuxp.codegen.swagger3.builder;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.macth.IgnoreMethodParameterMatchingStrategy;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.loong.CombinationCodeGenMatchingStrategy;
import com.wuxp.codegen.loong.LoongSimpleTemplateStrategy;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.swagger3.Swagger3CodeGenerator;
import com.wuxp.codegen.swagger3.Swagger3FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger3.languages.Swagger3FeignSdkJavaParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import com.wuxp.codegen.templates.TemplateLoader;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;


/**
 * @author wuxp
 */
@Builder
@Slf4j
public class Swagger3FeignJavaCodegenBuilder extends AbstractLoongCodegenBuilder {

    private final Boolean enabledAndroidSqliteSupport;

    private final Boolean useRxJava;

    @Override
    public CodeGenerator buildCodeGenerator() {
        LanguageDescription languageDescription = this.languageDescription;
        if (languageDescription == null) {
            languageDescription = LanguageDescription.JAVA;
        }
        if (this.clientProviderType == null) {
            this.clientProviderType = ClientProviderType.SPRING_CLOUD_OPENFEIGN;
        }
        this.codeGenMatchingStrategies.add(new Swagger3FeignSdkGenMatchingStrategy(this.ignoreMethods));
        if (!this.containsCollectionByType(codeGenMatchingStrategies, IgnoreMethodParameterMatchingStrategy.class)) {
            this.codeGenMatchingStrategies.add(IgnoreMethodParameterMatchingStrategy.of(this.ignoreParamByAnnotations));
        }
//        this.initTypeMapping();
        //实例化语言解析器
        LanguageParser languageParser = new Swagger3FeignSdkJavaParser(
                packageMapStrategy,
                CombinationCodeGenMatchingStrategy.of(this.codeGenMatchingStrategies),
                this.codeDetects,
                languageDescription,
                Boolean.TRUE.equals(useRxJava),
                Boolean.TRUE.equals(enabledAndroidSqliteSupport));
        initLanguageParser(languageParser);
        //实例化模板加载器
        TemplateLoader templateLoader = new FreemarkerTemplateLoader(clientProviderType, this.templateFileVersion, this.getSharedVariables());

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new LoongSimpleTemplateStrategy(
                templateLoader,
                this.outPath,
                languageDescription.getSuffixName(),
                this.isDeletedOutputDirectory, this.codeFormatter);

        return new Swagger3CodeGenerator(this.scanPackages, languageParser, templateStrategy, this.enableFieldUnderlineStyle, null)
                .otherCodegenClassMetas(otherCodegenClassMetas)
                .taskWaiters(Collections.singletonList(codeFormatter));
    }

    @Override
    protected void initAnnotationMetaFactory() {

    }

    @Override
    protected LanguageTypeDefinitionParser<? extends CommonCodeGenClassMeta> getMappingTypeDefinitionParser() {
        return null;
    }

    @Override
    protected List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ? extends Object>> getElementDefinitionParsers(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishParser) {
        return null;
    }
}
