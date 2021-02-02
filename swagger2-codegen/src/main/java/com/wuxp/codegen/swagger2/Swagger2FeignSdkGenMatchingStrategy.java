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
import java.util.Optional;

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
        Optional<Api> api = classMeta.getAnnotation(Api.class);
        return api.map(value -> !value.hidden()).orElse(true);
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
        Optional<ApiOperation> annotation = methodMeta.getAnnotation(ApiOperation.class);
        return annotation.map(value -> !value.hidden()).orElseGet(() -> {
            if (this.ignoreMethods == null) {
                return true;
            }

            String[] strings = this.ignoreMethods.get(methodMeta.getOwner());
            if (strings == null) {
                return true;
            }

            return !Arrays.asList(strings).contains(methodMeta.getName());
        });
    }

    @Override
    public boolean isMatchField(JavaFieldMeta javaFieldMeta) {

        return javaFieldMeta.getAnnotation(ApiModelProperty.class)
                .map(value -> !value.hidden())
                .orElse(true);
    }

    @Override
    public boolean isMatchParameter(JavaMethodMeta javaMethodMeta, Parameter parameter) {
        ApiParam apiParam = parameter.getAnnotation(ApiParam.class);
        if (apiParam != null) {
            return !apiParam.hidden();
        }
        String parameterName = parameter.getName();
        return javaMethodMeta.getAnnotation(ApiImplicitParams.class)
                .map(apiImplicitParams -> Arrays.stream(apiImplicitParams.value())
                        .filter(item -> item.name().equals(parameterName))
                        .findFirst())
                .orElseGet(() -> javaMethodMeta.getAnnotation(ApiImplicitParam.class))
                .map(apiImplicitParam -> !apiImplicitParam.readOnly())
                .orElse(true);
    }
}
