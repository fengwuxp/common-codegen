package com.wuxp.codegen;


import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.core.macth.IgnoreClassCodeGenMatcher;
import com.wuxp.codegen.core.macth.PackageNameCodeGenMatcher;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.enums.CodeRuntimePlatform;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;

import java.util.*;

import static com.wuxp.codegen.templates.TemplateLoader.CODE_RUNTIME_PLATFORM_KEY;

/**
 * 代码生成配置
 */
public abstract class AbstractDragonCodegenBuilder implements CodegenBuilder {


    protected LanguageDescription languageDescription = LanguageDescription.JAVA;

    /**
     * 扫码生成的包名
     */
    protected String[] scanPackages;


    /**
     * 输出路径
     */
    protected String outPath;


    /**
     * 基础数据类型的映射关系
     */
    protected Map<Class<?>, CommonCodeGenClassMeta> baseTypeMapping = new LinkedHashMap<>();


    /**
     * 自定义的类型映射
     */
    protected Map<Class<?>, CommonCodeGenClassMeta> customTypeMapping = new LinkedHashMap<>();

    /**
     * 自定义的java类型映射
     */
    protected Map<Class<?>, Class<?>[]> customJavaTypeMapping = new LinkedHashMap<>();

    /**
     * 忽略的包
     */
    protected Set<String> ignorePackages = new LinkedHashSet<>();

    /**
     * 包名映射策略
     */
    protected PackageMapStrategy packageMapStrategy;

    /**
     * 代码检测器
     */
    protected Collection<CodeDetect> codeDetects;

    /**
     * 是否删除输出目录
     */
    protected Boolean isDeletedOutputDirectory = true;

    /**
     * 是否使用宽松模式
     */
    protected boolean looseMode = false;

    /**
     * 启用下划线风格，将字段的驼峰名转换为下线命名风格
     */
    protected boolean enableFieldUnderlineStyle = false;


    /**
     * 运行平台
     */
    protected CodeRuntimePlatform codeRuntimePlatform;

    /**
     * 额外导入的类
     */
    protected Class<?>[] includeClasses;

    /**
     * 需要忽略的类
     */
    protected Class<?>[] ignoreClasses;

    /**
     * 忽略的方法
     */
    protected Map<Class<?>/*类名*/, String[]/*方法名称*/> ignoreMethods;


    protected AbstractDragonCodegenBuilder() {
    }

    public AbstractDragonCodegenBuilder scanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
        return this;
    }

    public AbstractDragonCodegenBuilder outPath(String outPath) {
        this.outPath = outPath;
        return this;
    }

    public AbstractDragonCodegenBuilder baseTypeMapping(Map<Class<?>, CommonCodeGenClassMeta> baseTypeMapping) {
        this.baseTypeMapping = baseTypeMapping;
        return this;
    }

    public AbstractDragonCodegenBuilder customTypeMapping(Map<Class<?>, CommonCodeGenClassMeta> customTypeMapping) {
        this.customTypeMapping = customTypeMapping;
        return this;
    }

    public AbstractDragonCodegenBuilder customJavaTypeMapping(Map<Class<?>, Class<?>[]> customJavaTypeMapping) {
        this.customJavaTypeMapping = customJavaTypeMapping;
        return this;
    }

    public AbstractDragonCodegenBuilder ignorePackages(Set<String> ignorePackages) {
        this.ignorePackages = ignorePackages;
        return this;
    }

    public AbstractDragonCodegenBuilder packageMapStrategy(PackageMapStrategy packageMapStrategy) {
        this.packageMapStrategy = packageMapStrategy;
        return this;
    }

    public AbstractDragonCodegenBuilder codeDetects(Collection<CodeDetect> codeDetects) {
        this.codeDetects = codeDetects;
        return this;
    }

    public AbstractDragonCodegenBuilder isDeletedOutputDirectory(Boolean isDeletedOutputDirectory) {
        this.isDeletedOutputDirectory = isDeletedOutputDirectory;
        return this;
    }

    public AbstractDragonCodegenBuilder languageDescription(LanguageDescription languageDescription) {
        this.languageDescription = languageDescription;
        return this;
    }

    public AbstractDragonCodegenBuilder looseMode(boolean looseMode) {
        this.looseMode = looseMode;
        return this;
    }

    public AbstractDragonCodegenBuilder enableFieldUnderlineStyle(boolean enableFieldUnderlineStyle) {
        this.enableFieldUnderlineStyle = enableFieldUnderlineStyle;
        return this;
    }

    public AbstractDragonCodegenBuilder codeRuntimePlatform(CodeRuntimePlatform codeRuntimePlatform) {
        this.codeRuntimePlatform = codeRuntimePlatform;
        return this;
    }

    public AbstractDragonCodegenBuilder includeClasses(Class<?>[] includeClasses) {
        this.includeClasses = includeClasses;
        return this;
    }


    public AbstractDragonCodegenBuilder ignoreClasses(Class<?>[] ignoreClasses) {
        this.ignoreClasses = ignoreClasses;
        return this;
    }

    public AbstractDragonCodegenBuilder ignoreMethods(Map<Class<?>/*类名*/, String[]/*方法名称*/> ignoreMethods) {
        this.ignoreMethods = ignoreMethods;
        return this;
    }

    protected void initTypeMapping() {
        //设置基础数据类型的映射关系
        baseTypeMapping.forEach((key, val) -> {
            AbstractTypeMapping.setBaseTypeMapping(key, val, true);
        });

        //自定义的类型映射
        customTypeMapping.forEach((key, val) -> {
            AbstractTypeMapping.setCustomizeTypeMapping(key, val, true);
        });

        //自定义的java类型映射
        customJavaTypeMapping.forEach((key, val) -> {
            AbstractTypeMapping.setCustomizeJavaTypeMapping(key, val, true);
        });


        PackageNameCodeGenMatcher.IGNORE_PACKAGE_LIST.addAll(ignorePackages);


        CodegenBuilder.CODEGEN_GLOBAL_CONFIG.setLanguageDescription(this.languageDescription);
    }

    /**
     * 获取模板的共享变量
     *
     * @return
     */
    protected Map<String, Object> getSharedVariables() {
        //全局共享变量
        Map<String, Object> sharedVariables = new HashMap<>();
        sharedVariables.put(CODE_RUNTIME_PLATFORM_KEY, codeRuntimePlatform.name());
        return sharedVariables;
    }
}
