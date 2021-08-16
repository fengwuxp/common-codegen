package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

import java.util.Arrays;

public class RemoveClientResponseTypePostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {


    private final CommonCodeGenClassMeta clientResponseType;

    public RemoveClientResponseTypePostProcessor(CommonCodeGenClassMeta clientResponseType) {
        this.clientResponseType = clientResponseType;
    }

    @Override
    public void postProcess(CommonCodeGenMethodMeta meta) {
        meta.setReturnTypes(removeClientResponseType(meta));
    }

    private CommonCodeGenClassMeta[] removeClientResponseType(CommonCodeGenMethodMeta meta) {
        return Arrays.stream(meta.getReturnTypes())
                .filter(classMeta -> !clientResponseType.equals(classMeta))
                .toArray(CommonCodeGenClassMeta[]::new);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenMethodMeta.class);
    }
}
