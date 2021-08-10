package com.wuxp.codegen.core.parser.enhance;

import com.wuxp.codegen.core.CodeGenClassSupportHandler;
import com.wuxp.codegen.model.CommonBaseMeta;

/**
 * @author wuxp
 */
@FunctionalInterface()
public interface LanguageDefinitionPostProcessor<C extends CommonBaseMeta> extends CodeGenClassSupportHandler {

    /**
     * 在解析完 {@link C} 对象后的 增强处理
     *
     * @param meta 元数据对象
     */
    void postProcess(C meta);

    @Override
    default boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CommonBaseMeta.class);
    }
}
