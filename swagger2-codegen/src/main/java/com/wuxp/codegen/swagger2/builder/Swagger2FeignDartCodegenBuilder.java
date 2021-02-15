package com.wuxp.codegen.swagger2.builder;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.annotation.processors.spring.RequestMappingProcessor;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.event.DisruptorCodeGenPublisher;
import com.wuxp.codegen.core.macth.IgnoreMethodParameterMatchingStrategy;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.disruptor.DartFeignCodeGenEventHandler;
import com.wuxp.codegen.loong.CombinationCodeGenMatchingStrategy;
import com.wuxp.codegen.loong.LoongSimpleTemplateStrategy;
import com.wuxp.codegen.languages.AbstractDartParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.swagger2.Swagger2CodeGenerator;
import com.wuxp.codegen.swagger2.Swagger2FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger2.languages.Swagger2FeignSdkDartParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import com.wuxp.codegen.templates.TemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @author wuxp
 */
@Slf4j
public class Swagger2FeignDartCodegenBuilder extends AbstractLoongCodegenBuilder {

    private Map<Class<?>, List<String>> ignoreFields;

    /**
     * sdk索引文件名称
     */
    private String sdkIndexFileName = "feign_sdk";

    /**
     * 类型别名
     */
    private Map<DartClassMeta, List<String>> typeAlias = Collections.emptyMap();

    public static Swagger2FeignDartCodegenBuilder builder() {
        return new Swagger2FeignDartCodegenBuilder();
    }

    public Swagger2FeignDartCodegenBuilder ignoreFields(Map<Class<?>, List<String>> ignoreFields) {
        this.ignoreFields = ignoreFields;
        return this;
    }

    public Swagger2FeignDartCodegenBuilder typeAlias(Map<DartClassMeta, List<String>> typeAlias) {
        this.typeAlias = typeAlias;
        return this;
    }

    public Swagger2FeignDartCodegenBuilder sdkIndexFileName(String sdkIndexFileName) {
        this.sdkIndexFileName = sdkIndexFileName;
        return this;
    }


    @Override
    public CodeGenerator buildCodeGenerator() {
        if (this.languageDescription == null) {
            this.languageDescription = LanguageDescription.DART;
        }

        if (this.clientProviderType == null) {
            this.clientProviderType = ClientProviderType.DART_FEIGN;
        }
        this.initTypeMapping();
        this.codeGenMatchingStrategies.add(new Swagger2FeignSdkGenMatchingStrategy(this.ignoreMethods));
        if (!this.containsCollectionByType(codeGenMatchingStrategies, IgnoreMethodParameterMatchingStrategy.class)) {
            this.codeGenMatchingStrategies.add(IgnoreMethodParameterMatchingStrategy.of(this.ignoreParamByAnnotations));
        }
        //实例化语言解析器
        LanguageParser languageParser = new Swagger2FeignSdkDartParser(
                packageMapStrategy,
                CombinationCodeGenMatchingStrategy.of(this.codeGenMatchingStrategies),
                this.codeDetects,
                this.ignoreFields);
        initLanguageParser(languageParser);

        //实例化模板加载器
        TemplateLoader templateLoader = new FreemarkerTemplateLoader(this.clientProviderType, this.templateFileVersion, this.getSharedVariables());

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new LoongSimpleTemplateStrategy(
                templateLoader,
                this.outPath,
                this.languageDescription.getSuffixName(),
                this.isDeletedOutputDirectory,
                filepath -> {
                    if (StringUtils.hasText(filepath)) {
                        return AbstractDartParser.dartFileNameConverter(filepath);
                    }
                    return filepath;
                });

        Thread currentThread = Thread.currentThread();
        DartFeignCodeGenEventHandler dartFeignCodeGenEventHandler = new DartFeignCodeGenEventHandler(templateLoader, this.outPath, currentThread);
        dartFeignCodeGenEventHandler.setSdkIndexFileName(this.sdkIndexFileName);
        dartFeignCodeGenEventHandler.setTypeAlias(this.typeAlias);
        RequestMappingProcessor.setSupportAuthenticationType(true);

        return new Swagger2CodeGenerator(
                this.scanPackages,
                this.ignorePackages,
                this.includeClasses.toArray(new Class[0]),
                this.ignoreClasses.toArray(new Class[0]),
                languageParser,
                templateStrategy,
                this.enableFieldUnderlineStyle,
                new DisruptorCodeGenPublisher(dartFeignCodeGenEventHandler))
                .otherCodegenClassMetas(otherCodegenClassMetas);
    }
}
