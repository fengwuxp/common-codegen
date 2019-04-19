package com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.AbstractCodeGenerator;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.ApiIgnore;


/**
 * swagger2 模式的代码生成
 */
@Slf4j
public class Swagger2CodeGenerator extends AbstractCodeGenerator {


    static {
        //设置扫描过滤器
        CANDIDATE_COMPONENT_PROVIDER.addExcludeFilter(new AnnotationTypeFilter(Component.class));
        CANDIDATE_COMPONENT_PROVIDER.addIncludeFilter(new AnnotationTypeFilter(Api.class));
        CANDIDATE_COMPONENT_PROVIDER.addExcludeFilter(new AnnotationTypeFilter(ApiIgnore.class));
    }

    public Swagger2CodeGenerator(String[] packagePaths,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy) {
        super(packagePaths, languageParser, templateStrategy);
    }
}
