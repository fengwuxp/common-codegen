package com.wuxp.codegen.swagger2.builder;

import com.wuxp.codegen.AbstractDragonCodegenBuilder;
import com.wuxp.codegen.annotation.processors.spring.RequestMappingProcessor;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.macth.IgnoreMethodParameterMatchingStrategy;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.CombinationCodeGenMatchingStrategy;
import com.wuxp.codegen.dragon.DragonSimpleTemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.swagger2.Swagger2CodeGenerator;
import com.wuxp.codegen.swagger2.Swagger2FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger2.languages.Swagger2FeignSdkTypescriptParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import com.wuxp.codegen.templates.TemplateLoader;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wuxp
 */
@Slf4j
public class Swagger2FeignTypescriptCodegenBuilder extends AbstractDragonCodegenBuilder {


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
        this.codeGenMatchingStrategies.add(new Swagger2FeignSdkGenMatchingStrategy(this.ignoreMethods));
        if (!this.containsCollectionByType(codeGenMatchingStrategies, IgnoreMethodParameterMatchingStrategy.class)) {
            this.codeGenMatchingStrategies.add(IgnoreMethodParameterMatchingStrategy.of(this.ignoreParamByAnnotations));
        }
        this.initTypeMapping();
        //实例化语言解析器
        LanguageParser languageParser = new Swagger2FeignSdkTypescriptParser(
                packageMapStrategy,
                CombinationCodeGenMatchingStrategy.of(this.codeGenMatchingStrategies),
                this.codeDetects);
        initLanguageParser(languageParser);
        //实例化模板加载器
        TemplateLoader templateLoader = new FreemarkerTemplateLoader(this.clientProviderType, this.templateFileVersion, this.getSharedVariables());

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new DragonSimpleTemplateStrategy(
                templateLoader,
                this.outPath,
                LanguageDescription.TYPESCRIPT.getSuffixName(),
                this.isDeletedOutputDirectory);
        RequestMappingProcessor.setSupportAuthenticationType(true);

        return new Swagger2CodeGenerator(
                this.scanPackages,
                this.ignorePackages,
                this.includeClasses.toArray(new Class[0]),
                this.ignoreClasses.toArray(new Class[0]),
                languageParser,
                templateStrategy,
                this.enableFieldUnderlineStyle)
                .otherCodegenClassMetas(otherCodegenClassMetas);
    }
}
