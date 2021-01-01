package com.wuxp.codegen.swagger3.builder;

import com.wuxp.codegen.AbstractDragonCodegenBuilder;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.DragonSimpleTemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.swagger3.Swagger3CodeGenerator;
import com.wuxp.codegen.swagger3.Swagger3FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger3.languages.Swagger3FeignSdkJavaParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import com.wuxp.codegen.templates.TemplateLoader;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;


/**
 * @author wuxp
 */
@Builder
@Slf4j
public class Swagger3FeignJavaCodegenBuilder extends AbstractDragonCodegenBuilder {

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
        this.initTypeMapping();
        //实例化语言解析器
        LanguageParser languageParser = new Swagger3FeignSdkJavaParser(
                packageMapStrategy,
                new Swagger3FeignSdkGenMatchingStrategy(this.ignoreMethods),
                this.codeDetects,
                languageDescription,
                Boolean.TRUE.equals(useRxJava),
                Boolean.TRUE.equals(enabledAndroidSqliteSupport));
        initLanguageParser(languageParser);
        //实例化模板加载器
        TemplateLoader templateLoader = new FreemarkerTemplateLoader(clientProviderType, this.templateFileVersion, this.getSharedVariables());

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new DragonSimpleTemplateStrategy(
                templateLoader,
                this.outPath,
                languageDescription.getSuffixName(),
                this.isDeletedOutputDirectory);

        return new Swagger3CodeGenerator(this.scanPackages, languageParser, templateStrategy, this.enableFieldUnderlineStyle, null)
                .otherCodegenClassMetas(otherCodegenClassMetas);
    }
}
