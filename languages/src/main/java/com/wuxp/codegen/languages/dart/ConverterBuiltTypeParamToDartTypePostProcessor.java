package com.wuxp.codegen.languages.dart;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

import java.util.Arrays;
import java.util.Map;

/**
 * 将请求参数的中的简单参数的集合类型 从 Built 的集合转换为 Dart 的标准集合对象
 */
public class ConverterBuiltTypeParamToDartTypePostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {

    @Override
    public void postProcess(CommonCodeGenMethodMeta meta) {
        Map<String, CommonCodeGenClassMeta> params = meta.getParams();
        params.forEach((key, paramType) -> {
            CommonCodeGenClassMeta[] typeVariables = Arrays.stream(paramType.getTypeVariables()).map(this::converterBuiltType).toArray(CommonCodeGenClassMeta[]::new);
            paramType.setTypeVariables(typeVariables);
        });
    }

    private CommonCodeGenClassMeta converterBuiltType(CommonCodeGenClassMeta typeVariableType) {
        if (DartClassMeta.BUILT_LIST.equals(typeVariableType)) {
            return DartClassMeta.LIST;
        }
        if (DartClassMeta.BUILT_MAP.equals(typeVariableType)) {
            return DartClassMeta.MAP;
        }
        if (DartClassMeta.BUILT_SET.equals(typeVariableType)) {
            return DartClassMeta.SET;
        }
        if (DartClassMeta.BUILT_ITERABLE.equals(typeVariableType)) {
            return DartClassMeta.ITERABLE;
        }
        return typeVariableType;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenMethodMeta.class);
    }
}
