package com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 基于swagger2的生成匹配策略
 */
@Slf4j
public class Swagger2CodeGenMatchingStrategy implements CodeGenMatchingStrategy {


    @Override
    public boolean isMatchClazz(JavaClassMeta classMeta) {
        return !classMeta.existAnnotation(ApiIgnore.class);
    }

    @Override
    public boolean isMatchMethod(JavaMethodMeta methodMeta) {
        return !methodMeta.existAnnotation(ApiIgnore.class);
    }
}
