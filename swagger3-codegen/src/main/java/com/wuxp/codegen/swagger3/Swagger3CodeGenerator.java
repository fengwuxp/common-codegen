package com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.event.CodeGenPublisher;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.languages.AnnotationMetaHolder;
import com.wuxp.codegen.loong.AbstractCodeGenerator;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.swagger3.annotations.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * @author wxup
 */
@Slf4j
public class Swagger3CodeGenerator extends AbstractCodeGenerator {

    static {
        //添加swagger3相关的注解处理器
        AnnotationMetaHolder.registerAnnotationProcessor(Operation.class, new OperationProcessor());
        AnnotationMetaHolder.registerAnnotationProcessor(ApiResponse.class, new ApiResponseProcessor());
        AnnotationMetaHolder.registerAnnotationProcessor(Parameter.class, new ParameterProcessor());
        AnnotationMetaHolder.registerAnnotationProcessor(Parameters.class, new ParametersProcessor());
        AnnotationMetaHolder.registerAnnotationProcessor(RequestBody.class, new RequestBodyProcessor());
        AnnotationMetaHolder.registerAnnotationProcessor(Schema.class, new SchemaProcessor());
        AnnotationMetaHolder.registerAnnotationProcessor(Tag.class, new TagProcessor());
    }

    {
        //设置扫描过滤器
        classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(Tag.class));
        classPathScanningCandidateComponentProvider.addExcludeFilter(new AnnotationTypeFilter(Hidden.class));
    }

    public Swagger3CodeGenerator(String[] packagePaths,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                                 boolean enableFieldUnderlineStyle,
                                 CodeGenPublisher codeGenPublisher) {
        super(packagePaths, languageParser, templateStrategy, enableFieldUnderlineStyle, codeGenPublisher);
    }


    public Swagger3CodeGenerator(String[] packagePaths,
                                 Set<String> ignorePackages,
                                 Class<?>[] includeClasses,
                                 Class<?>[] ignoreClasses,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                                 boolean enableFieldUnderlineStyle,
                                 CodeGenPublisher codeGenPublisher) {
        super(packagePaths, ignorePackages, includeClasses, ignoreClasses, languageParser, templateStrategy, enableFieldUnderlineStyle, codeGenPublisher);
    }
}
