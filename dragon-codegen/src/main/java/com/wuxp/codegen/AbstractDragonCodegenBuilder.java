package com.wuxp.codegen;


import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.retrofit2.Retrofit2AnnotationProvider;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.core.macth.PackageNameCodeGenMatcher;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.core.strategy.AbstractPackageMapStrategy;
import com.wuxp.codegen.core.strategy.ClassNameTransformer;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.TemplateFileVersion;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;

import java.util.*;

/**
 * 代码生成配置
 *
 * @author wxup
 */
public abstract class AbstractDragonCodegenBuilder implements CodegenBuilder {

    static {
        AbstractAnnotationProcessor.registerAnnotationProvider(ClientProviderType.RETROFIT, new Retrofit2AnnotationProvider());
    }


    protected LanguageDescription languageDescription;

    protected ClientProviderType clientProviderType;

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
     * 类名映射策略
     *
     * @key 类名或ant匹配
     * @value 字符串或 ClassNameTransformer
     */
    private Map<String, Object> classNameTransformers;

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
     * 模板文件版本
     *
     * @see TemplateFileVersion
     */
    protected String templateFileVersion = TemplateFileVersion.DEFAULT.getVersion();

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
     *
     * @key 类
     * @value 方法名称
     */
    protected Map<Class<?>, String[]> ignoreMethods;

    /**
     * 语言处理增强器
     */
    protected LanguageEnhancedProcessor languageEnhancedProcessor = LanguageEnhancedProcessor.NONE;

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

    /**
     * @param classNameTransformers
     * @return
     * @see ClassNameTransformer
     */
    public AbstractDragonCodegenBuilder classNameTransformers(Map<String/*类名或ant匹配*/, Object/*字符串或 ClassNameTransformer*/> classNameTransformers) {
        this.classNameTransformers = classNameTransformers;
        if (this.packageMapStrategy != null && this.packageMapStrategy instanceof AbstractPackageMapStrategy) {
            ((AbstractPackageMapStrategy) this.packageMapStrategy).setClassNameTransformers(classNameTransformers);
        }
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

    public AbstractDragonCodegenBuilder clientProviderType(ClientProviderType clientProviderType) {
        this.clientProviderType = clientProviderType;
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


    public AbstractDragonCodegenBuilder templateFileVersion(String templateFileVersion) {
        this.templateFileVersion = templateFileVersion;
        return this;
    }

    public AbstractDragonCodegenBuilder templateFileVersion(TemplateFileVersion templateFileVersion) {
        this.templateFileVersion = templateFileVersion.getVersion();
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

    public AbstractDragonCodegenBuilder languageEnhancedProcessor(LanguageEnhancedProcessor languageEnhancedProcessor) {
        this.languageEnhancedProcessor = languageEnhancedProcessor;
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
        CodegenBuilder.CODEGEN_GLOBAL_CONFIG.setProviderType(this.clientProviderType);

    }

    /**
     * 获取模板的共享变量
     *
     * @return
     */
    protected Map<String, Object> getSharedVariables() {
        //全局共享变量
        Map<String, Object> sharedVariables = new HashMap<>();
        return sharedVariables;
    }
}
