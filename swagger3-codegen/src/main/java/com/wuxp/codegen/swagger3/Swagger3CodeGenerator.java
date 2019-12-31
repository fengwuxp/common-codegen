package com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.AbstractCodeGenerator;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 *
 */
@Slf4j
public class Swagger3CodeGenerator extends AbstractCodeGenerator {

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
