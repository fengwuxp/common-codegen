package com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

/**
 * 基于swagger2的生成 feign api sdk的匹配策略
 *
 * @author wuxp
 */
@Slf4j
public class Swagger2FeignSdkGenMatchingStrategy implements CodeGenMatchingStrategy {

    /**
     * 忽略的方法
     *
     * @key 类名
     * @value 方法名称
     */
    protected Map<Class<?>, String[]> ignoreMethods;

    public Swagger2FeignSdkGenMatchingStrategy() {
    }

    public Swagger2FeignSdkGenMatchingStrategy(Map<Class<?>, String[]> ignoreMethods) {
        this.ignoreMethods = ignoreMethods;
    }

    @Override
    public boolean isMatchClazz(JavaClassMeta classMeta) {
        if (classMeta.existAnnotation(ApiIgnore.class)) {
            return false;
        }
        Api api = classMeta.getAnnotation(Api.class);
        if (api != null) {
            return !api.hidden();
        }
        return true;
    }

    @Override
    public boolean isMatchMethod(JavaMethodMeta methodMeta) {
        boolean b = !methodMeta.existAnnotation(ApiIgnore.class);
        if (!b) {
            return false;
        }
        if (methodMeta.existAnnotation(ApiIgnore.class)) {
            return false;
        }

        ApiOperation annotation = methodMeta.getAnnotation(ApiOperation.class);
        if (annotation != null) {
            if (!annotation.hidden()) {
                return true;
            }
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
        ApiModelProperty apiModelProperty = javaFieldMeta.getAnnotation(ApiModelProperty.class);
        if (apiModelProperty == null) {
            return true;
        }
        return !apiModelProperty.hidden();
    }

    @Override
    public boolean isMatchParameter(JavaMethodMeta javaMethodMeta, Parameter parameter) {
        ApiParam apiParam = parameter.getAnnotation(ApiParam.class);
        if (apiParam != null) {
            return !apiParam.hidden();
        }
        ApiImplicitParams parameters = javaMethodMeta.getAnnotation(ApiImplicitParams.class);
        ApiImplicitParam p = null;
        if (parameters != null) {
            ApiImplicitParam[] value = parameters.value();
            if (value.length > 0) {
                p = Arrays.stream(value)
                        .filter(item -> item.name().equals(parameter.getName())).findFirst()
                        .orElse(null);
            }

        } else {
            p = javaMethodMeta.getAnnotation(ApiImplicitParam.class);
        }
        if (p == null) {
            return true;
        }
        return !p.readOnly();
    }
}
