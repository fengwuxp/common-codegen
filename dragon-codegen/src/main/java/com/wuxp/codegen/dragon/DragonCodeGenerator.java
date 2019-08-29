package com.wuxp.codegen.dragon;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;


/**
 * dragon 模式的代码生成
 */
@Slf4j
public class DragonCodeGenerator extends AbstractCodeGenerator {


    public DragonCodeGenerator(String[] packagePaths,
                               LanguageParser<CommonCodeGenClassMeta> languageParser,
                               TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                               boolean enableFieldUnderlineStyle) {
        super(packagePaths, languageParser, templateStrategy, enableFieldUnderlineStyle);
    }
}
