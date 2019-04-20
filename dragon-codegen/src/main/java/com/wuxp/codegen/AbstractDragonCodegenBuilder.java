package com.wuxp.codegen;


import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;

import java.util.Collection;
import java.util.Map;

/**
 * 代码生成配置
 */
public abstract class AbstractDragonCodegenBuilder implements CodegenBuilder {

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
    protected Map<Class<?>, ? extends CommonCodeGenClassMeta> baseTypeMapping;

    /**
     * 自定义的类型映射
     */
    protected Map<Class<?>, Class<?>[]> customTypeMapping;

    /**
     * 包名映射策略
     */
    protected PackageMapStrategy packageMapStrategy;

    /**
     * 代码检测器
     */
    protected Collection<CodeDetect> codeDetects;


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

    public AbstractDragonCodegenBuilder baseTypeMapping(Map<Class<?>, ? extends CommonCodeGenClassMeta> baseTypeMapping) {
        this.baseTypeMapping = baseTypeMapping;
        return this;
    }

    public AbstractDragonCodegenBuilder customTypeMapping(Map<Class<?>, Class<?>[]> customTypeMapping) {
        this.customTypeMapping = customTypeMapping;
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

    protected void initTypeMapping() {
        //设置基础数据类型的映射关系
        baseTypeMapping.forEach((key, val) -> {
            AbstractTypeMapping.BASE_TYPE_MAPPING.put(key, val);
        });

        //自定义的类型映射
        customTypeMapping.forEach((key, val) -> {
            AbstractTypeMapping.CUSTOMIZE_TYPE_MAPPING.put(key, val);
        });


    }
}
