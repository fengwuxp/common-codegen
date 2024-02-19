package com.wuxp.codegen.swagger3.builder;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.languages.CommonFieldDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.languages.java.JavaTypeDefinitionParser;
import com.wuxp.codegen.languages.java.JavaTypeVariableDefinitionParser;
import com.wuxp.codegen.languages.java.RxJavaSupportPostProcessor;
import com.wuxp.codegen.mapping.MappingJavaTypeDefinitionParser;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


/**
 * @author wuxp
 */
@Builder
@Slf4j
public class Swagger3FeignJavaCodegenBuilder extends AbstractSwagger3CodegenBuilder {

    private final Boolean useRxJava;

    @Override
    public CodeGenerator buildCodeGenerator() {
        initCodegenConfig(LanguageDescription.JAVA, ClientProviderType.SPRING_CLOUD_OPENFEIGN);
        if (ClientProviderType.RETROFIT.equals(this.clientProviderType) && Boolean.TRUE.equals(useRxJava)) {
            this.elementParsePostProcessors(new RxJavaSupportPostProcessor());
        }
        configParserPostProcessors(JavaCodeGenClassMeta.RX_JAVA2_OBSERVABLE);
        configCodeGenElementMatchers();
        return createCodeGenerator();
    }

    @Override
    protected LanguageTypeDefinitionParser<? extends CommonCodeGenClassMeta> getMappingTypeDefinitionParser() {
        return MappingJavaTypeDefinitionParser.builder()
                .typeMapping(typeMappings)
                .build();
    }

    @Override
    protected List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ? extends Object>> getElementDefinitionParsers(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishParser) {
        List<LanguageElementDefinitionParser<? extends CommonBaseMeta, ?>> parsers = Arrays.asList(
                new JavaTypeDefinitionParser(publishParser, this.getPackageMapStrategy()),
                getLanguageMethodDefinitionParser(publishParser),
                new CommonFieldDefinitionParser(publishParser),
                new JavaTypeVariableDefinitionParser()
        );
        configureElementParsers(parsers);
        return parsers;
    }
}
