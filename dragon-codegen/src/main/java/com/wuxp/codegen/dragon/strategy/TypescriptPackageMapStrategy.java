package com.wuxp.codegen.dragon.strategy;

import com.wuxp.codegen.dragon.core.strategy.AbstractPackageMapStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

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

        if (!StringUtils.hasText(path)) {
            log.warn("{}装换后的导入路径为空", clazz.getName());
            return "";
        }

        String convertClassName = this.convertClassName(path.replaceAll("\\.", "/"));
        if (convertClassName.startsWith("/")) {
            return convertClassName;
        }
        return "/" + convertClassName;

    }



}
