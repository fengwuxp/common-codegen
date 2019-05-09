package com.wuxp.codegen;


import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.core.config.CodegenGlobalConfig;
import com.wuxp.codegen.core.macth.PackageNameCodeGenMatcher;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;

import java.util.*;

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
    ;

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

    protected void initTypeMapping() {
        //设置基础数据类型的映射关系
        baseTypeMapping.forEach(AbstractTypeMapping.BASE_TYPE_MAPPING::put);

        //自定义的类型映射
        customTypeMapping.forEach(AbstractTypeMapping.CUSTOMIZE_TYPE_MAPPING::put);

        //自定义的java类型映射
        customJavaTypeMapping.forEach(AbstractTypeMapping.CUSTOMIZE_JAVA_TYPE_MAPPING::put);


        PackageNameCodeGenMatcher.IGNORE_PACKAGE_LIST.addAll(ignorePackages);


        CodegenBuilder.CODEGEN_GLOBAL_CONFIG.setLanguageDescription(this.languageDescription);
    }
}
