package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

/**
 * @author wuxp
 */
public interface LanguageFieldDefinitionParser<F extends CommonCodeGenFiledMeta> extends LanguageElementDefinitionParser<F, JavaFieldMeta> {

    @Override
    default boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaFieldMeta.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    default F newElementInstance() {
        return (F) new CommonCodeGenFiledMeta();
    }
}
