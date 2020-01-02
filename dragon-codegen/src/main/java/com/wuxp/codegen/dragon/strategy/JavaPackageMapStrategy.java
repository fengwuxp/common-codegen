package com.wuxp.codegen.dragon.strategy;

import com.wuxp.codegen.core.strategy.AbstractPackageMapStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Map;

@Slf4j
public class JavaPackageMapStrategy extends AbstractPackageMapStrategy {


    private String basePackage;

    public JavaPackageMapStrategy(Map<String, String> packageNameMap, String basePackage) {
        super(packageNameMap);
        this.basePackage = basePackage;
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
        String packageName = String.join(".", uris);
        if (!packageName.startsWith(this.basePackage)) {
            packageName = MessageFormat.format("{0}.{1}", basePackage, packageName);
        }

        return packageName;
    }
}
