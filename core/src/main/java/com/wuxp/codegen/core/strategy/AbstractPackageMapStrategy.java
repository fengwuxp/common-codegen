package com.wuxp.codegen.core.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;


/**
 * 抽象的包名映射策略
 */
@Slf4j
public abstract class AbstractPackageMapStrategy implements PackageMapStrategy {

    protected Map<String/*类的包名前缀*/, String/*output path*/> packageNameMap;

    protected PathMatcher pathMatcher = new AntPathMatcher();


    public AbstractPackageMapStrategy(Map<String, String> packageNameMap) {
        this.packageNameMap = packageNameMap;
    }

    @Override
    public String convert(Class<?> clazz) {
        //包名
        Package aPackage = clazz.getPackage();
        String packageNames = "";
        String clazzName = clazz.getName();
        if (aPackage == null) {
            log.warn("包名为空的类{}", clazzName);
//            return null;
        } else {
            packageNames = aPackage.getName();
        }
        Optional<String> packageNamePrefix = this.packageNameMap.keySet()
                .stream()
//                .filter(clazzName::startsWith)
                .map(pattern -> {
                    if (pattern.endsWith("**")) {
                        return pattern;
                    }

                    return MessageFormat.format("{0}**", pattern);
                }).filter(pattern -> this.pathMatcher.match(pattern, clazzName))
                .findFirst();
        //没有找到可以替换的前缀
        if (!packageNamePrefix.isPresent()) {
            //直接返回类名
            return clazz.getSimpleName();
        }

        String key = packageNamePrefix.get();
        String val = this.packageNameMap.get(key.substring(0, key.length() - 2));

        if (val == null) {
            throw new RuntimeException(MessageFormat.format("包名：{0} 未找到装换映射关系", clazzName));
        }
        if (!StringUtils.hasText(val)) {

        }


        return clazzName.replace(packageNames, val);
    }

    @Override
    public String convertClassName(String className) {
        //默认将Controller 转换为Service
        return className.replaceAll("Controller", "Service");
    }
}
