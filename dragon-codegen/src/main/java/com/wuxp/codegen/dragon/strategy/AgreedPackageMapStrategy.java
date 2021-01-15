package com.wuxp.codegen.dragon.strategy;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.dragon.path.PathResolve;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 基于约定的包名映射策略
 *
 * @author wuxp
 */
public class AgreedPackageMapStrategy implements PackageMapStrategy {

    private static final List<String> IGNORE_NAMES = Arrays.asList(
            "controller",
            "services",
            "action",
            "enums",
            "dto",
            "model"
    );

    @Override
    public String convert(Class<?> clazz) {
        List<String> basePackages = CodegenConfigHolder.getConfig().getBasePackages();
        String groupId = basePackages.stream()
                .filter(basePackage -> clazz.getName().startsWith(basePackage))
                .findFirst()
                .orElse("");
        String base;
        if (clazz.isEnum()) {
            base = "enums";
        } else {
            boolean isServerClass = CodegenConfigHolder.getConfig().isServerClass(clazz);
            if (isServerClass) {
                base = "clients";
            } else {
                base = "model";
            }
        }
        boolean isJava = CodegenConfigHolder.getConfig().isJava();
        String outPackage = getOutPackage(clazz, String.format("%s.%s", groupId, base), groupId);
        String className = convertClassName(clazz);
        if (isJava) {
            return String.format("%s.%s", outPackage, className);
        }
        String path = outPackage.replace(groupId, "").replaceAll("\\.", PathResolve.RIGHT_SLASH);
        return String.format("%s%s%s%s", PathResolve.RIGHT_SLASH, path, PathResolve.RIGHT_SLASH, className);
    }

    @Override
    public String convertClassName(Class<?> clazz) {
        boolean isServerClass = CodegenConfigHolder.getConfig().isServerClass(clazz);
        String simpleName = clazz.getSimpleName();
        if (isServerClass) {
            return convertServiceClassName(simpleName, getSuffixName());
        }
        return simpleName;
    }

    @Override
    public String genPackagePath(String[] uris) {
        boolean isJava = CodegenConfigHolder.getConfig().isJava();
        if (isJava) {
            List<String> basePackages = CodegenConfigHolder.getConfig().getBasePackages();
            String url = String.join(".", uris);
            if (basePackages.size() == 1) {
                // base package
                String basePackage = basePackages.get(0);
                return basePackage + "." + url;
            }
            return url;
        }
        return MessageFormat.format("/{0}", String.join("/", uris));
    }

    protected String convertServiceClassName(String simpleName, String suffixName) {
        if (simpleName.endsWith("Controller")) {
            return simpleName.replaceAll("Controller", suffixName);
        }
        if (simpleName.endsWith("Action")) {
            return simpleName.replaceAll("Action", suffixName);
        }
        return simpleName;
    }


    private String getOutPackage(Class<?> clazz, String base, String basePackage) {
        Package aPackage = clazz.getPackage();
        if (aPackage == null) {
            return base;
        }
        String packageName = aPackage.getName();
        if (packageName.startsWith(basePackage)) {
            String[] names = packageName.replace(basePackage, "").split("\\.");
            int length = names.length;
            String lastName = names[length - 1];
            if (length > 2 && !IGNORE_NAMES.contains(lastName)) {
                return String.format("%s.%s", base, lastName);
            }
        }
        return base;
    }

    private String getSuffixName() {
        ClientProviderType providerType = CodegenConfigHolder.getConfig().getProviderType();
        switch (providerType) {
            case TYPESCRIPT_FEIGN:
            case OPENFEIGN:
            case SPRING_CLOUD_OPENFEIGN:
                return "FeignClient";
            case RETROFIT:
                return "RetrofitClient";
            case UMI_REQUEST:
            default:
                return "Service";
        }

    }
}
