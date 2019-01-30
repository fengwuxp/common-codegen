package com.wuxp.codegen.dragon.templates;


/**
 * 抽象的模板加载器
 */
public abstract class AbstractTemplateLoader<T> implements TemplateLoader<T>{

    /**
     * 需要加载模板的语言类型
     */
    protected String language;

    public AbstractTemplateLoader(String language) {
        this.language = language;
    }
}
