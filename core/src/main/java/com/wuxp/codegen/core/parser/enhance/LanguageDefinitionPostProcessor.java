package com.wuxp.codegen.core.parser.enhance;

import com.wuxp.codegen.model.CommonBaseMeta;

/**
 * @author wuxp
 */
@FunctionalInterface()
public interface LanguageDefinitionPostProcessor<C extends CommonBaseMeta> {

    /**
     * 在解析完 {@link C} 对象后的 增强处理
     * @param meta 元数据对象
     * @return 元数据对象
     */
     C postProcess(C meta);
}
