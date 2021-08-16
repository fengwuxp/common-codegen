package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @see com.wuxp.codegen.meta.enums.EnumDefinitionPostProcessor
 */
public class EnumNamesPostProcessor implements LanguageDefinitionPostProcessor<TypescriptClassMeta> {

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, TypescriptClassMeta.class);
    }

    @Override
    public void postProcess(TypescriptClassMeta meta) {
        if (ObjectUtils.isEmpty(meta.getEnumConstants())) {
            return;
        }
        String enumNames = Arrays.stream(meta.getEnumConstants())
                .map(CommonCodeGenFiledMeta::getName)
                .map(name -> String.format("'%s'", name))
                .collect(Collectors.joining(" | "));
        meta.setEnumNames(enumNames);
        meta.setNeedImport(false);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
