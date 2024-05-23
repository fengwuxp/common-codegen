package com.wuxp.codegen.loong.strategy;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.loong.path.PathResolve;
import com.wuxp.codegen.core.util.CodegenFileUtils;

import java.io.File;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于约定的包名映射策略
 * <p>
 * 以下的输出目录都基于sdk生成的根目录进行计算
 * 1：接口生成在clients目录下
 * 2：枚举生成在enums目录下
 * 3：数据传输对象生成在model
 * {@link #innerConvert}
 * </p>
 *
 * @author wuxp
 */
public class AgreedPackageMapStrategy implements PackageNameConvertStrategy {

    private static final List<String> IGNORE_NAMES = Arrays.asList(
            "controller",
            "services",
            "action",
            "enums",
            "dto",
            "model"
    );

    private final Map<Class<?>, String> convertCache = new HashMap<>();

    private final Map<Class<?>, String> convertClassnameCache = new HashMap<>();

    @Override
    public String convert(Class<?> clazz) {
        return convertCache.computeIfAbsent(clazz, key -> innerConvert(clazz));
    }


    @Override
    public String convertClassName(Class<?> clazz) {
        return convertClassnameCache.computeIfAbsent(clazz, key -> {
            boolean isServerClass = CodegenConfigHolder.getConfig().isServerClass(clazz);
            String simpleName = clazz.getSimpleName();
            if (isServerClass) {
                return convertServiceClassName(simpleName, getSuffixName());
            }
            return simpleName;
        });
    }

    @Override
    public String genPackagePath(String[] uris) {
        boolean isJava = CodegenConfigHolder.getConfig().isJava();
        if (isJava) {
            List<String> basePackages = CodegenConfigHolder.getConfig().getBasePackages();
            String url = String.join(".", uris);
            if (url.startsWith(".")) {
                url = url.substring(1);
            }
            if (basePackages.size() == 1) {
                // base package
                String basePackage = basePackages.get(0);
                return basePackage + "." + url;
            }
            return url;
        }
        String result = String.join(PathResolve.RIGHT_SLASH, uris);
        if (result.startsWith(PathResolve.RIGHT_SLASH)) {
            return result;
        }
        return MessageFormat.format("/{0}", result);
    }

    private String innerConvert(Class<?> clazz) {
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
        String packageFormat = "%s.%s";
        String outPackage = getOutPackage(clazz, String.format(packageFormat, groupId, base), groupId);
        String className = convertClassName(clazz);
        if (isJava) {
            return String.format(packageFormat, outPackage, className);
        }
        String path = CodegenFileUtils.toFilepathPart(outPackage.replace(groupId, ""))
                .replace(File.separator,PathResolve.RIGHT_SLASH);
        return String.format("%s%s%s%s", PathResolve.RIGHT_SLASH, path, PathResolve.RIGHT_SLASH, className);
    }


    protected String convertServiceClassName(String simpleName, String suffixName) {
        if (simpleName.endsWith("Controller")) {
            return simpleName.replace("Controller", suffixName);
        }
        if (simpleName.endsWith("Action")) {
            return simpleName.replace("Action", suffixName);
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
            case TYPESCRIPT_FEIGN_FUNC:
            case OPENFEIGN:
            case SPRING_CLOUD_OPENFEIGN:
                return "FeignClient";
            case RETROFIT:
                return "RetrofitClient";
            case UMI_REQUEST:
            case AXIOS:
            default:
                return "Service";
        }

    }
}
