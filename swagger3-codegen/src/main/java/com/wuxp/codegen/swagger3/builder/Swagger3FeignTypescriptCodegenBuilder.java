package com.wuxp.codegen.swagger3.builder;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.macth.IgnoreMethodParameterMatchingStrategy;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.loong.CombinationCodeGenMatchingStrategy;
import com.wuxp.codegen.loong.LoongSimpleTemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.swagger3.Swagger3CodeGenerator;
import com.wuxp.codegen.swagger3.Swagger3FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger3.languages.Swagger3FeignSdkTypescriptParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import com.wuxp.codegen.templates.TemplateLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

/**
 * @author wuxp
 */
@Slf4j
public class Swagger3FeignTypescriptCodegenBuilder extends AbstractLoongCodegenBuilder {


    protected Swagger3FeignTypescriptCodegenBuilder() {
        super();
    }


    public static Swagger3FeignTypescriptCodegenBuilder builder() {
        return new Swagger3FeignTypescriptCodegenBuilder();
    }

    @Override
    public CodeGenerator buildCodeGenerator() {
        if (this.languageDescription == null) {
            this.languageDescription = LanguageDescription.TYPESCRIPT;
        }
        if (this.clientProviderType == null) {
            this.clientProviderType = ClientProviderType.TYPESCRIPT_FEIGN;
        }
        this.codeGenMatchingStrategies.add(new Swagger3FeignSdkGenMatchingStrategy(this.ignoreMethods));
        if (!this.containsCollectionByType(codeGenMatchingStrategies, IgnoreMethodParameterMatchingStrategy.class)) {
            this.codeGenMatchingStrategies.add(IgnoreMethodParameterMatchingStrategy.of(this.ignoreParamByAnnotations));
        }
        this.initTypeMapping();
        //实例化语言解析器
        LanguageParser languageParser = new Swagger3FeignSdkTypescriptParser(
                packageMapStrategy,
                CombinationCodeGenMatchingStrategy.of(this.codeGenMatchingStrategies),
                this.codeDetects);
        initLanguageParser(languageParser);

        //实例化模板加载器
        TemplateLoader templateLoader = new FreemarkerTemplateLoader(this.clientProviderType, this.templateFileVersion, this.getSharedVariables());

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new LoongSimpleTemplateStrategy(
                templateLoader,
                this.outPath,
                LanguageDescription.TYPESCRIPT.getSuffixName(),
                this.isDeletedOutputDirectory,this.codeFormatter);
        RequestMappingMetaFactory.setSupportAuthenticationType(true);
        return new Swagger3CodeGenerator(
                this.scanPackages,
                this.ignorePackages,
                this.includeClasses.toArray(new Class[0]),
                this.ignoreClasses.toArray(new Class[0]),
                languageParser,
                templateStrategy,
                this.enableFieldUnderlineStyle, null)
                .otherCodegenClassMetas(otherCodegenClassMetas)
                .taskWaiters(Collections.singletonList(codeFormatter));
    }
}
