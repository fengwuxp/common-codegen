package com.oaknt.codegen;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;


/**
 * oak 模式的代码生成
 */
@Slf4j
public class OAKCodeGenerator extends AbstractCodeGenerator {


    public OAKCodeGenerator(String[] packagePaths, LanguageParser<CommonCodeGenClassMeta> languageParser, TemplateStrategy<CommonCodeGenClassMeta> templateStrategy) {
        super(packagePaths, languageParser, templateStrategy);
    }
}
