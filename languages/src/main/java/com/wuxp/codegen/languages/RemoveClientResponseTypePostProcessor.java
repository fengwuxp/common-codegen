package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

import java.util.Arrays;
import java.util.Objects;

/**
 * 移除重复的 client 统一响应类型
 *
 * @author wuxp
 */
public class RemoveClientResponseTypePostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {

    /**
     * client 统一响应类型
     */
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
                .filter(classMeta -> !isResponseType(classMeta))
                .toArray(CommonCodeGenClassMeta[]::new);
    }

    private boolean isResponseType(CommonCodeGenClassMeta meta) {
        return Objects.equals(meta, clientResponseType) ||
                Objects.equals(meta.getSource(), clientResponseType.getSource()) ||
                Objects.equals(meta.getPackagePath(), clientResponseType.getPackagePath());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenMethodMeta.class);
    }
}
