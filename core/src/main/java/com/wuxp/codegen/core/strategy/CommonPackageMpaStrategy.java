package com.wuxp.codegen.core.strategy;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 通用的包名映射策略
 */
@Slf4j
public class CommonPackageMpaStrategy extends AbstractPackageMapStrategy {

    public CommonPackageMpaStrategy(Map<String, String> packageNameMap) {
        super(packageNameMap);
    }
}
