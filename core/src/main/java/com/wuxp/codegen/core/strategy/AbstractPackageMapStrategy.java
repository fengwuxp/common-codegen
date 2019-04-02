package com.wuxp.codegen.core.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;


/**
 * 抽象的包名映射策略
 */
@Slf4j
public abstract class AbstractPackageMapStrategy implements PackageMapStrategy {

    protected Map<String/*类的包名前缀*/, String/*output path*/> packageNameMap;


    public AbstractPackageMapStrategy(Map<String, String> packageNameMap) {
        this.packageNameMap = packageNameMap;
    }

    @Override
    public String convert(Class<?> clazz) {
        //包名
        Package aPackage = clazz.getPackage();
        String clazzName = clazz.getName();
        if (aPackage == null) {
            log.warn("包名为空的类{}", clazzName);
//            return null;
        }
        Optional<String> packageNamePrefix = this.packageNameMap.keySet()
                .stream()
                .filter(clazzName::startsWith)
                .findFirst();
        //没有找到可以替换的前缀
        if (!packageNamePrefix.isPresent()) {
            //直接返回类名
            return clazz.getSimpleName();
        }

        String key = packageNamePrefix.get();
        String val = this.packageNameMap.get(key);

        if (val == null) {
            throw new RuntimeException("包名：" + clazzName + " 未找到装换映射关系");
        }
        if (!StringUtils.hasText(val)) {

        }

        return clazzName.replace(key, val);
    }

    @Override
    public String convertClassName(String className) {
        //将Controller 转换为Service
        return className.replaceAll("Controller", "Service");
    }
}
