package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 移除重复的 client 统一响应类型
 *
 * @author wuxp
 */
public class RemoveRepeatClientResponseTypePostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {

    /**
     * client 统一响应类型
     */
    private final CommonCodeGenClassMeta clientResponseType;

    public RemoveRepeatClientResponseTypePostProcessor(CommonCodeGenClassMeta clientResponseType) {
        this.clientResponseType = clientResponseType;
    }

    @Override
    public void postProcess(CommonCodeGenMethodMeta meta) {
        meta.setReturnTypes(removeClientResponseType(meta));
    }

    private CommonCodeGenClassMeta[] removeClientResponseType(CommonCodeGenMethodMeta meta) {
        List<CommonCodeGenClassMeta> result = new ArrayList<>(meta.getReturnTypes().length);
        int count = 0;
        for (CommonCodeGenClassMeta classMeta : meta.getReturnTypes()) {
            if (isResponseType(classMeta)) {
                if (count > 0) {
                    // 存在重复的响应类型，移除
                    continue;
                }
                count++;
            }
            result.add(classMeta);
        }
        return result.toArray(new CommonCodeGenClassMeta[0]);
    }

    private boolean isResponseType(CommonCodeGenClassMeta meta) {
        return Objects.equals(meta, clientResponseType) ||
                Objects.equals(meta.getSource(), clientResponseType.getSource());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenMethodMeta.class);
    }
}
