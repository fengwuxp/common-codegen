package com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.AbstractCodeGenerator;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.swagger2.annotations.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import static com.wuxp.codegen.languages.AbstractLanguageParser.ANNOTATION_PROCESSOR_MAP;


/**
 * swagger2 模式的代码生成
 */
@Slf4j
public class Swagger2CodeGenerator extends AbstractCodeGenerator {


    static {
        //添加swagger相关的注解处理器
        ANNOTATION_PROCESSOR_MAP.put(Api.class, new ApiProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiModel.class, new ApiModelProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiModelProperty.class, new ApiModelPropertyProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiOperation.class, new ApiOperationProcessor());
        ANNOTATION_PROCESSOR_MAP.put(ApiParam.class, new ApiParamProcessor());
    }


    {
        //设置扫描过滤器
        classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(Api.class));

        classPathScanningCandidateComponentProvider.addExcludeFilter(new AnnotationTypeFilter(ApiIgnore.class));
    }

    public Swagger2CodeGenerator(String[] packagePaths,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy) {
        super(packagePaths, languageParser, templateStrategy);
    }

    public Swagger2CodeGenerator(String[] packagePaths,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy, boolean looseMode) {
        this(packagePaths, languageParser, templateStrategy);
        if (looseMode) {
            classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
            classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        }
    }
}
