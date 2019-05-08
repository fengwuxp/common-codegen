package com.wuxp.codegen.swagger2.builder;

import com.wuxp.codegen.AbstractDragonCodegenBuilder;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.DragonSimpleTemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.swagger2.Swagger2CodeGenerator;
import com.wuxp.codegen.swagger2.Swagger2FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger2.languages.Swagger2FeignSdkJavaParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import com.wuxp.codegen.templates.TemplateLoader;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;


@Builder
@Slf4j
public class Swagger2FeignJavaCodegenBuilder extends AbstractDragonCodegenBuilder {


    protected LanguageDescription languageDescription = LanguageDescription.JAVA;


    @Override
    public CodeGenerator buildCodeGenerator() {


        //实例化语言解析器
        LanguageParser languageParser = new Swagger2FeignSdkJavaParser(
                packageMapStrategy,
                new Swagger2FeignSdkGenMatchingStrategy(),
                this.codeDetects);


        //实例化模板加载器
        TemplateLoader templateLoader = new FreemarkerTemplateLoader(this.languageDescription);

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new DragonSimpleTemplateStrategy(
                templateLoader,
                this.outPath,
                this.languageDescription.getSuffixName(),
                this.isDeletedOutputDirectory);

        return new Swagger2CodeGenerator(this.scanPackages, languageParser, templateStrategy);
    }
}
