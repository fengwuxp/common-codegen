package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

import java.util.ArrayList;
import java.util.List;

public class RemoveClientResponseTypePostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {


    private final CommonCodeGenClassMeta clientResponseType;

    public RemoveClientResponseTypePostProcessor(CommonCodeGenClassMeta clientResponseType) {
        this.clientResponseType = clientResponseType;
    }

    @Override
    public void postProcess(CommonCodeGenMethodMeta meta) {
        CommonCodeGenClassMeta[] returnTypes = meta.getReturnTypes();
        List<CommonCodeGenClassMeta> types = new ArrayList<>(returnTypes.length);
        for (CommonCodeGenClassMeta classMeta : returnTypes) {
            if (!clientResponseType.equals(classMeta)) {
                types.add(classMeta);
            }
        }
        meta.setReturnTypes(types.toArray(new CommonCodeGenClassMeta[0]));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenMethodMeta.class);
    }
}
