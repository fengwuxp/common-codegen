package com.wuxp.codegen.core.config;


import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.model.LanguageDescription;
import lombok.Builder;
import lombok.Data;

/**
 * 代码生成的全局配置
 *
 * @author wxup
 */
@Data
@Builder
public final class CodegenGlobalConfig {


    private LanguageDescription languageDescription;

    private ClientProviderType providerType;


}
