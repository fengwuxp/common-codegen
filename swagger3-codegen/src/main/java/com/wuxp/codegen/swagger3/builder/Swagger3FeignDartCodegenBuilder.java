package com.wuxp.codegen.swagger3.builder;

import com.wuxp.codegen.AbstractDragonCodegenBuilder;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.FileNameGenerateStrategy;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.DragonSimpleTemplateStrategy;
import com.wuxp.codegen.dragon.path.PathResolve;
import com.wuxp.codegen.enums.CodeRuntimePlatform;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.swagger3.Swagger3CodeGenerator;
import com.wuxp.codegen.swagger3.Swagger3FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger3.languages.Swagger3FeignSdkDartParser;
import com.wuxp.codegen.swagger3.languages.Swagger3FeignSdkJavaParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import com.wuxp.codegen.templates.TemplateLoader;
import com.wuxp.codegen.utils.JavaMethodNameUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;


@Builder
@Slf4j
public class Swagger3FeignDartCodegenBuilder extends AbstractDragonCodegenBuilder {


    protected Swagger3FeignDartCodegenBuilder() {
        super();
    }


    public static Swagger3FeignDartCodegenBuilder builder() {
        return new Swagger3FeignDartCodegenBuilder();
    }


    @Override
    public CodeGenerator buildCodeGenerator() {

        if (this.languageDescription == null) {
            this.languageDescription = LanguageDescription.DART;
        }

        if (this.codeRuntimePlatform == null) {
            this.codeRuntimePlatform = CodeRuntimePlatform.JAVA_SERVER;
        }
        this.initTypeMapping();
        //实例化语言解析器
        LanguageParser languageParser = new Swagger3FeignSdkDartParser(
                packageMapStrategy,
                new Swagger3FeignSdkGenMatchingStrategy(this.ignoreMethods),
                this.codeDetects);


        //实例化模板加载器
        TemplateLoader templateLoader = new FreemarkerTemplateLoader(this.languageDescription, this.templateFileVersion, this.getSharedVariables());

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new DragonSimpleTemplateStrategy(
                templateLoader,
                this.outPath,
                this.languageDescription.getSuffixName(),
                this.isDeletedOutputDirectory,
                filepath -> {
                    String[] split = filepath.split(PathResolve.RIGHT_SLASH);
                    String s = split[split.length - 1];
                    split[split.length - 1] = JavaMethodNameUtil.humpToLine(s);
                    return String.join(PathResolve.RIGHT_SLASH, split);
                });


        return new Swagger3CodeGenerator(this.scanPackages, languageParser, templateStrategy, this.enableFieldUnderlineStyle);
    }
}
