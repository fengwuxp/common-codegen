package com.wuxp.codegen.dragon.core.strategy;


import com.wuxp.codegen.dragon.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.dragon.model.languages.java.JavaMethodMeta;

/**
 * 生成的匹配策略
 * <p>
 * 匹配目标（类，或方法）是否需要生成
 * </p>
 */
public interface CodeGenMatchingStrategy {

    /**
     * 类是否匹配
     *
     * @param classMeta
     * @return
     */
    boolean isMatchClazz(JavaClassMeta classMeta);

    /**
     * 方法是否匹配
     *
     * @param methodMeta
     * @return
     */
    boolean isMatchMethod(JavaMethodMeta methodMeta);

}
