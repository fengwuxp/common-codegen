package com.wuxp.codegen.loong;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;


/**
 * loong 模式的代码生成
 *
 * @author wuxp
 */
@Slf4j
public class LoongCodeGenerator extends AbstractCodeGenerator {


  public LoongCodeGenerator(String[] packagePaths,
                            LanguageParser<CommonCodeGenClassMeta> languageParser,
                            TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                            boolean enableFieldUnderlineStyle) {
    super(packagePaths, languageParser, templateStrategy, enableFieldUnderlineStyle);
  }


}
