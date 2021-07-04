package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.LanguageFieldDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;

/**
 * @author wuxp
 */
public class AbstractLanguageFieldDefinitionParser<F extends CommonCodeGenFiledMeta> implements LanguageFieldDefinitionParser<F> {


    @Override
    public F parse(JavaFieldMeta source) {
        return null;
    }

    @Override
    public F newInstance() {
        return null;
    }
}
