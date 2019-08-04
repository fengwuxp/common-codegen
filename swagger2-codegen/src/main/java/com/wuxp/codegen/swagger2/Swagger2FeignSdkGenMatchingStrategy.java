package com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Map;

/**
 * 基于swagger2的生成 feign api sdk的匹配策略
 */
@Slf4j
public class Swagger2FeignSdkGenMatchingStrategy implements CodeGenMatchingStrategy {

    /**
     * 忽略的方法
     */
    protected Map<Class<?>/*类名*/, String[]/*方法名称*/> ignoreMethods;

    public Swagger2FeignSdkGenMatchingStrategy() {
    }

    public Swagger2FeignSdkGenMatchingStrategy(Map<Class<?>, String[]> ignoreMethods) {
        this.ignoreMethods = ignoreMethods;
    }

    @Override
    public boolean isMatchClazz(JavaClassMeta classMeta) {
        return !classMeta.existAnnotation(ApiIgnore.class);
    }

    @Override
    public boolean isMatchMethod(JavaMethodMeta methodMeta) {
        boolean b = !methodMeta.existAnnotation(ApiIgnore.class);
        if (!b) {
            return false;
        }

        Map<Class<?>, String[]> ignoreMethods = this.ignoreMethods;
        if (ignoreMethods ==null){
            return true;
        }

        String[] strings = this.ignoreMethods.get(methodMeta.getOwner());
        if (strings == null) {
            return true;
        }

        return !Arrays.asList(strings).contains(methodMeta.getName());
    }
}
