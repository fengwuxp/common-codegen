package com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.AbstractCodeGenerator;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.swagger3.annotations.OperationProcessor;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static com.wuxp.codegen.languages.AbstractLanguageParser.ANNOTATION_PROCESSOR_MAP;

/**
 *
 */
@Slf4j
public class Swagger3CodeGenerator extends AbstractCodeGenerator {

    static {
        //添加swagger3相关的注解处理器
        ANNOTATION_PROCESSOR_MAP.put(Operation.class, new OperationProcessor());
    }


    public Swagger3CodeGenerator(String[] packagePaths, LanguageParser<CommonCodeGenClassMeta> languageParser, TemplateStrategy<CommonCodeGenClassMeta> templateStrategy, boolean enableFieldUnderlineStyle) {
        super(packagePaths, languageParser, templateStrategy, enableFieldUnderlineStyle);
    }

    public Swagger3CodeGenerator(String[] packagePaths, Set<String> ignorePackages, Class<?>[] includeClasses, Class<?>[] ignoreClasses, LanguageParser<CommonCodeGenClassMeta> languageParser, TemplateStrategy<CommonCodeGenClassMeta> templateStrategy, boolean enableFieldUnderlineStyle) {
        super(packagePaths, ignorePackages, includeClasses, ignoreClasses, languageParser, templateStrategy, enableFieldUnderlineStyle);
    }


    @Override
    protected Set<Class<?>> scanPackages() {
        return super.scanPackages();
    }
}
