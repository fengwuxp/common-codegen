package com.wuxp.codegen.core.config;

import com.wuxp.codegen.model.LanguageDescription;

/**
 * 使用 {@link ThreadLocal} 保存配置
 *
 * @author wuxp
 */
public final class CodegenConfigHolder {

    private static final ThreadLocal<CodegenConfig> GLOBAL_CONFIG = new ThreadLocal<>();

    private CodegenConfigHolder() {
    }

    public static CodegenConfig getConfig() {

        return GLOBAL_CONFIG.get();
    }

    public static void setConfig(CodegenConfig config) {
        GLOBAL_CONFIG.set(config);
    }

    public static void clear() {
        GLOBAL_CONFIG.remove();
    }

    public static LanguageDescription getCurrentLanguageDescription() {
        CodegenConfig config = getConfig();
        if (config == null) {
            return null;
        }
        return config.getLanguageDescription();
    }

    public static boolean isEnabledCodeFormatter() {
        CodegenConfig config = getConfig();
        if (config == null) {
            return false;
        }
        return config.isEnabledCodeFormatter();
    }
}
