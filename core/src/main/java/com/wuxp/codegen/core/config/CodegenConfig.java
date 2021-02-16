package com.wuxp.codegen.core.config;


import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 代码生成配置
 *
 * @author wxup
 */
@Data
@Builder
public final class CodegenConfig {


    /**
     * 不同语言默认的统一响应类型的默认值
     */
    private static final Map<LanguageDescription, CommonCodeGenClassMeta> DEFAULT_UNIFIED_RESPONSE_TYPE_MAPPING = new HashMap<>();

    /**
     * 属于api 服务的注解
     */
    private static final Set<Class<? extends Annotation>> DEFAULT_API_SERVICE_ANNOTATIONS = new LinkedHashSet<>();

    static {
        DEFAULT_UNIFIED_RESPONSE_TYPE_MAPPING.put(LanguageDescription.TYPESCRIPT, TypescriptClassMeta.PROMISE);
        DEFAULT_UNIFIED_RESPONSE_TYPE_MAPPING.put(LanguageDescription.DART, DartClassMeta.FUTRUE);

        DEFAULT_API_SERVICE_ANNOTATIONS.add(Controller.class);
        DEFAULT_API_SERVICE_ANNOTATIONS.add(RequestMapping.class);
        DEFAULT_API_SERVICE_ANNOTATIONS.add(RestController.class);
    }

    /**
     * 需要生成的语言
     */
    private LanguageDescription languageDescription;

    /**
     * client provider type
     */
    private ClientProviderType providerType;

    /**
     * 统一的响应类型
     */
    private CommonCodeGenClassMeta unifiedResponseType;

    /**
     * 用于标记的某个类是接口的注解
     */
    private Set<Class<? extends Annotation>> servicesAnnotations;

    /**
     * 基础包名
     */
    private List<String> basePackages;

    /**
     * 是否启用代码格式化
     * 默认不启用
     */
    private boolean enabledCodeFormatter;


    public boolean isJava() {
        return LanguageDescription.JAVA.equals(languageDescription) || LanguageDescription.JAVA_ANDROID.equals(languageDescription);
    }

    public CommonCodeGenClassMeta getUnifiedResponseType() {
        if (unifiedResponseType == null) {
            return DEFAULT_UNIFIED_RESPONSE_TYPE_MAPPING.get(languageDescription);
        }
        return unifiedResponseType;
    }

    public Set<Class<? extends Annotation>> getApiMarkedAnnotations() {
        if (servicesAnnotations == null) {
            return DEFAULT_API_SERVICE_ANNOTATIONS;
        }
        return servicesAnnotations;
    }

    public boolean isServerClass(Class<?> clazz) {
        return getApiMarkedAnnotations().stream().anyMatch(clazz::isAnnotationPresent);
    }

    public List<String> getBasePackages() {
        if (basePackages == null) {
            basePackages = new ArrayList<>();
        }
        return basePackages;
    }

}
