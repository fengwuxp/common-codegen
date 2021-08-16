package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.languages.AbstractLanguageTypeDefinitionParser;
import com.wuxp.codegen.languages.LanguageTypeDefinitionPublishParser;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author wuxp
 */
public class TypeScriptTypeDefinitionParser extends AbstractLanguageTypeDefinitionParser<TypescriptClassMeta> {

    public TypeScriptTypeDefinitionParser(LanguageTypeDefinitionPublishParser<?> languageTypeDefinitionPublishParser,
                                          PackageNameConvertStrategy packageNameConvertStrategy) {
        super(languageTypeDefinitionPublishParser, packageNameConvertStrategy);
    }

    @Override
    public TypescriptClassMeta parse(Class<?> source) {
        TypescriptClassMeta result = super.parse(source);
        processEnumNames(result);
        return result;
    }

    private void processEnumNames(TypescriptClassMeta result) {
        if (result == null || ObjectUtils.isEmpty(result.getEnumConstants())) {
            return;
        }
        String enumNames = Arrays.stream(result.getEnumConstants())
                .map(CommonCodeGenFiledMeta::getName)
                .map(name -> String.format("'%s'", name))
                .collect(Collectors.joining(" | "));
        result.setEnumNames(enumNames);
        result.setNeedImport(false);
    }

    @Override
    public TypescriptClassMeta newElementInstance() {
        return new TypescriptClassMeta();
    }

}
