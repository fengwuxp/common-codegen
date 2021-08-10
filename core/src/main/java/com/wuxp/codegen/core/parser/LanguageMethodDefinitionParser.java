package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

/**
 * @author wuxp
 */
public interface LanguageMethodDefinitionParser<M extends CommonCodeGenMethodMeta> extends LanguageElementDefinitionParser<M, JavaMethodMeta> {


    @Override
    default boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaMethodMeta.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    default M newElementInstance() {
        return (M) new CommonCodeGenMethodMeta();
    }
}
