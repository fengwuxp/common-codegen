package com.wuxp.codegen.templates;

/**
 * 模板加载器
 * <p>
 *     1：根据配置加载模板
 *     2：根据生成的目标语言加载模板
 *     3：根据生成模型加载模板
 * </p>
 */
public interface TemplateLoader<T> {

    /**
     * 加载模板
     * @param templateName
     * @return
     */
    T load(String templateName);
}
