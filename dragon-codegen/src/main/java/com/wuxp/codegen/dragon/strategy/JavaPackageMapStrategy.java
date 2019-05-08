package com.wuxp.codegen.dragon.strategy;

import com.wuxp.codegen.core.strategy.AbstractPackageMapStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
public class JavaPackageMapStrategy extends AbstractPackageMapStrategy {


    public JavaPackageMapStrategy(Map<String, String> packageNameMap) {
        super(packageNameMap);
    }

    @Override
    public String convert(Class<?> clazz) {
        String path = super.convert(clazz);
        if (!StringUtils.hasText(path)) {
            log.warn("{}转换后的导入的路径为空", clazz.getName());
            return clazz.getSimpleName();
        }
        return this.convertClassName(path);

    }

    @Override
    public String genPackagePath(String[] uris) {
        return String.join(".", uris);
    }
}
