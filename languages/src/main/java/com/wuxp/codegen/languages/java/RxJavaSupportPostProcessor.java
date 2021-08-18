package com.wuxp.codegen.languages.java;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于增强生成 rxjava
 */
public class RxJavaSupportPostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {

    @Override
    public void postProcess(CommonCodeGenMethodMeta meta) {
        meta.setReturnTypes(addRxjavaObservableType(meta.getReturnTypes()));
    }

    private CommonCodeGenClassMeta[] addRxjavaObservableType(CommonCodeGenClassMeta[] metas) {
        List<CommonCodeGenClassMeta> result = Arrays.stream(metas).collect(Collectors.toList());
        if (!result.contains(JavaCodeGenClassMeta.RX_JAVA2_OBSERVABLE)) {
            result.add(0, JavaCodeGenClassMeta.RX_JAVA2_OBSERVABLE);
        }
        return result.toArray(new CommonCodeGenClassMeta[0]);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenMethodMeta.class);
    }
}
