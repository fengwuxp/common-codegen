package com.wuxp.codegen.templates;


import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.TemplateFileVersion;

/**
 * 抽象的模板加载器
 * @author wuxp
 */
public abstract class AbstractTemplateLoader<T> implements TemplateLoader<T> {

    /**
     * 需要加载模板的语言类型
     */
    protected final ClientProviderType clientProviderType;

    protected final String templateFileVersion;

    public AbstractTemplateLoader(ClientProviderType clientProviderType) {
        this(clientProviderType, TemplateFileVersion.DEFAULT.getVersion());
    }

    public AbstractTemplateLoader(ClientProviderType clientProviderType, String templateFileVersion) {
        this.clientProviderType = clientProviderType;
        this.templateFileVersion = templateFileVersion;
    }
}
