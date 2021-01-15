package com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.event.CodeGenPublisher;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.AbstractCodeGenerator;
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

import static com.wuxp.codegen.languages.AbstractLanguageParser.ANNOTATION_PROCESSOR_MAP;

/**
 * @author wxup
 */
@Slf4j
public class Swagger3CodeGenerator extends AbstractCodeGenerator {

    static {
        //添加swagger3相关的注解处理器
        ANNOTATION_PROCESSOR_MAP.put(Operation.class, new OperationProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiResponse.class, new ApiResponseProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Parameter.class, new ParameterProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Parameters.class, new ParametersProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestBody.class, new RequestBodyProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Schema.class, new SchemaProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Tag.class, new TagProcessor());
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
