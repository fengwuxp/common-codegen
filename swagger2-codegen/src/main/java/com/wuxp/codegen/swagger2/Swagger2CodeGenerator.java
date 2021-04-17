package com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.event.CodeGenPublisher;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.languages.AnnotationMetaFactoryHolder;
import com.wuxp.codegen.loong.AbstractCodeGenerator;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.swagger2.annotations.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Set;


/**
 * swagger2 模式的代码生成
 *
 * @author wuxp
 */
@Slf4j
public class Swagger2CodeGenerator extends AbstractCodeGenerator {


    static {
        //添加swagger相关的注解处理器
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(Api.class, new ApiMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiModel.class, new ApiModelMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiModelProperty.class, new ApiModelPropertyMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiOperation.class, new ApiOperationMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiParam.class, new ApiParamMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiImplicitParam.class, new ApiImplicitParamMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiImplicitParams.class, new ApiImplicitParamsMetaFactory());
    }


    {
        //设置扫描过滤器
        classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(Api.class));
        classPathScanningCandidateComponentProvider.addExcludeFilter(new AnnotationTypeFilter(ApiIgnore.class));
    }

    public Swagger2CodeGenerator(String[] packagePaths,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                                 boolean enableFieldUnderlineStyle) {
        super(packagePaths, languageParser, templateStrategy, enableFieldUnderlineStyle);
    }


    public Swagger2CodeGenerator(String[] packagePaths,
                                 Set<String> ignorePackages,
                                 Class<?>[] includeClasses,
                                 Class<?>[] ignoreClasses,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                                 boolean enableFieldUnderlineStyle) {
        super(packagePaths, ignorePackages, includeClasses, ignoreClasses, languageParser, templateStrategy, enableFieldUnderlineStyle, null);
    }

    public Swagger2CodeGenerator(String[] packagePaths,
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
