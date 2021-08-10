package com.wuxp.codegen.swagger2.builder;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.macth.IgnoreMethodParameterMatchingStrategy;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.loong.CombinationCodeGenMatchingStrategy;
import com.wuxp.codegen.loong.LoongSimpleTemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.swagger2.Swagger2CodeGenerator;
import com.wuxp.codegen.swagger2.Swagger2FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger2.languages.Swagger2FeignSdkJavaParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import com.wuxp.codegen.templates.TemplateLoader;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;


/**
 * @author wuxp
 */
@Builder
@Slf4j
public class Swagger2FeignJavaCodegenBuilder extends AbstractLoongCodegenBuilder {

    private final Boolean enabledAndroidSqliteSupport;

    private final Boolean useRxJava;

    @Override
    public CodeGenerator buildCodeGenerator() {
        if (this.languageDescription == null) {
            this.languageDescription = LanguageDescription.JAVA;
        }
        if (this.clientProviderType == null) {
            this.clientProviderType = ClientProviderType.SPRING_CLOUD_OPENFEIGN;
        }
        this.codeGenMatchingStrategies.add(new Swagger2FeignSdkGenMatchingStrategy(this.ignoreMethods));
        if (!this.containsCollectionByType(codeGenMatchingStrategies, IgnoreMethodParameterMatchingStrategy.class)) {
            this.codeGenMatchingStrategies.add(IgnoreMethodParameterMatchingStrategy.of(this.ignoreParamByAnnotations));
        }
        this.initTypeMapping();
        //实例化语言解析器
        LanguageParser languageParser = new Swagger2FeignSdkJavaParser(
                packageMapStrategy,
                CombinationCodeGenMatchingStrategy.of(this.codeGenMatchingStrategies),
                this.codeDetects,
                languageDescription,
                Boolean.TRUE.equals(useRxJava),
                Boolean.TRUE.equals(enabledAndroidSqliteSupport));
        initLanguageParser(languageParser);

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new LoongSimpleTemplateStrategy(
                getTemplateLoader(),
                this.outPath,
                this.languageDescription.getSuffixName(),
                this.isDeletedOutputDirectory, this.codeFormatter);


        return new Swagger2CodeGenerator(this.scanPackages, languageParser, templateStrategy, this.enableFieldUnderlineStyle)
                .otherCodegenClassMetas(otherCodegenClassMetas)
                .taskWaiters(Collections.singletonList(codeFormatter));
    }

}
