package com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

/**
 * 基于swagger2的生成 feign api sdk的匹配策略
 * @author wuxp
 */
@Slf4j
public class Swagger3FeignSdkGenMatchingStrategy implements CodeGenMatchingStrategy {

    /**
     * 忽略的方法
     *
     * @key 类名
     * @value 方法名称
     */
    protected Map<Class<?>, String[]> ignoreMethods;

    public Swagger3FeignSdkGenMatchingStrategy() {
    }

    public Swagger3FeignSdkGenMatchingStrategy(Map<Class<?>, String[]> ignoreMethods) {
        this.ignoreMethods = ignoreMethods;
    }

    @Override
    public boolean isMatchClazz(JavaClassMeta classMeta) {
        return !classMeta.existAnnotation(Hidden.class);
    }

    @Override
    public boolean isMatchMethod(JavaMethodMeta methodMeta) {
        boolean b = !methodMeta.existAnnotation(Hidden.class);
        if (!b) {
            return false;
        }

        Map<Class<?>, String[]> ignoreMethods = this.ignoreMethods;
        if (ignoreMethods == null) {
            return true;
        }

        String[] strings = this.ignoreMethods.get(methodMeta.getOwner());
        if (strings == null) {
            return true;
        }

        return !Arrays.asList(strings).contains(methodMeta.getName());
    }

    @Override
    public boolean isMatchField(JavaFieldMeta javaFieldMeta) {
        Schema schema = javaFieldMeta.getAnnotation(Schema.class);
        if (schema == null) {
            return true;
        }
        return !schema.hidden();
    }

    @Override
    public boolean isMatchParameter(JavaMethodMeta javaMethodMeta, Parameter parameter) {
        Schema schema = parameter.getAnnotation(Schema.class);
        if (schema != null) {
            return !schema.hidden();
        }
        Parameters parameters = javaMethodMeta.getAnnotation(Parameters.class);
        io.swagger.v3.oas.annotations.Parameter p = null;
        if (parameters != null) {
            io.swagger.v3.oas.annotations.Parameter[] value = parameters.value();
            if (value.length > 0) {
                p = Arrays.stream(value)
                        .filter(item -> item.name().equals(parameter.getName())).findFirst()
                        .orElse(null);
            }
        } else {
            p = javaMethodMeta.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class);
        }
        if (p == null) {
            return true;
        }
        return !p.hidden();
    }
}
