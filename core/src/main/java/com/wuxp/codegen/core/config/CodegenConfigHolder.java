package com.wuxp.codegen.core.config;

/**
 * 使用 {@link ThreadLocal} 保存配置
 *
 * @author wuxp
 */
public class CodegenConfigHolder {

  private final static ThreadLocal<CodegenConfig> GLOBAL_CONFIG = new ThreadLocal<>();

  public static CodegenConfig getConfig() {

    return GLOBAL_CONFIG.get();
  }

  public static void setConfig(CodegenConfig config) {
    GLOBAL_CONFIG.set(config);
  }

  public static void clear() {
    GLOBAL_CONFIG.remove();
  }
}
