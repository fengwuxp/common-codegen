package com.wuxp.codegen.templates;


import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.TemplateFileVersion;

/**
 * 抽象的模板加载器
 */
public abstract class AbstractTemplateLoader<T> implements TemplateLoader<T> {

    /**
     * 需要加载模板的语言类型
     */
    protected LanguageDescription language;

    protected String templateFileVersion;

    public AbstractTemplateLoader(LanguageDescription language) {
        this(language, TemplateFileVersion.DEFAULT.getVersion());
    }

    public AbstractTemplateLoader(LanguageDescription language, String templateFileVersion) {
        this.language = language;
        this.templateFileVersion = templateFileVersion;
    }
}
