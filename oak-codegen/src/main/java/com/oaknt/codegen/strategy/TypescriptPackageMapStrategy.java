package com.oaknt.codegen.strategy;

import com.wuxp.codegen.core.strategy.AbstractPackageMapStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


/**
 * typeScript 映射策略
 */
@Slf4j
public class TypescriptPackageMapStrategy extends AbstractPackageMapStrategy {


    public TypescriptPackageMapStrategy(Map<String, String> packageNameMap) {
        super(packageNameMap);
    }

    @Override
    public String convert(Class<?> clazz) {
        String path = super.convert(clazz);

        return path.replaceAll("\\.", "\\\\");
    }
}
