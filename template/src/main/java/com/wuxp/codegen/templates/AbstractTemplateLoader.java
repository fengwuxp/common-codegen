package com.wuxp.codegen.templates;


import com.wuxp.codegen.model.LanguageDescription;

/**
 * 抽象的模板加载器
 */
public abstract class AbstractTemplateLoader<T> implements TemplateLoader<T>{

    /**
     * 需要加载模板的语言类型
     */
    protected LanguageDescription language;

    public AbstractTemplateLoader(LanguageDescription language) {
        this.language = language;
    }
}
