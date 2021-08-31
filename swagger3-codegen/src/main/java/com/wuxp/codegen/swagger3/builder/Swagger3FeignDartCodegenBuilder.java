package com.wuxp.codegen.swagger3.builder;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.languages.CommonFieldDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.languages.dart.DartCodeGenEventListener;
import com.wuxp.codegen.languages.dart.DartTypeDefinitionParser;
import com.wuxp.codegen.languages.dart.DartTypeVariableDefinitionParser;
import com.wuxp.codegen.mapping.MappingDartTypeDefinitionParser;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * @author wxup
 */
@Slf4j
public class Swagger3FeignDartCodegenBuilder extends AbstractSwagger3CodegenBuilder {

    /**
     * 类型别名
     */
    private Map<DartClassMeta, List<String>> typeAlias = Collections.emptyMap();

    private String feignSdkLibName;

    public static Swagger3FeignDartCodegenBuilder builder() {
        return new Swagger3FeignDartCodegenBuilder();
    }

    public Swagger3FeignDartCodegenBuilder typeAlias(Map<DartClassMeta, List<String>> typeAlias) {
        this.typeAlias = typeAlias;
        return this;
    }

    public Swagger3FeignDartCodegenBuilder feignSdkLibName(String feignSdkLibName) {
        this.feignSdkLibName = feignSdkLibName;
        return this;
    }

    @Override
    public CodeGenerator buildCodeGenerator() {
        initCodegenConfig(LanguageDescription.DART, ClientProviderType.DART_FEIGN);
        configParserPostProcessors(DartClassMeta.FUTURE);
        configCodeGenElementMatchers();
        this.codeGenEventListeners(new DartCodeGenEventListener(feignSdkLibName, typeAlias));
        return createCodeGenerator();
    }

    @Override
    protected LanguageTypeDefinitionParser<? extends CommonCodeGenClassMeta> getMappingTypeDefinitionParser() {
        return MappingDartTypeDefinitionParser.builder()
                .typeMapping(baseTypeMapping)
                .build();
    }

    @Override
    protected List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ? extends Object>> getElementDefinitionParsers(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishParser) {
        List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ?>> parsers = Arrays.asList(
                new DartTypeDefinitionParser(publishParser, this.getPackageMapStrategy()),
                getLanguageMethodDefinitionParser(publishParser),
                new CommonFieldDefinitionParser(publishParser),
                new DartTypeVariableDefinitionParser()
        );
        configureElementParsers(parsers);
        return parsers;
    }
}
