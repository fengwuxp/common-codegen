package com.wuxp.codegen.core.strategy;

import lombok.extern.slf4j.Slf4j;

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
        if (aPackage==null){
            log.error("不需要生成的类{}",clazz.getName());
            return null;
        }
        String packageName = aPackage.getName();
        Optional<String> packageNamePrefix = this.packageNameMap.keySet()
                .stream()
                .filter(packageName::startsWith)
                .findFirst();
        if (!packageNamePrefix.isPresent()) {
            return "";
        }

        String key = packageNamePrefix.get();
        String val = this.packageNameMap.get(key);

        if (val == null) {
            throw new RuntimeException("包名：" + packageName + " 未找到装换映射关系");
        }
        return val;
    }
}
