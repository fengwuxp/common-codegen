package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 方法是否支持生成
 */
public class JavaMethodMatcher implements CodeGenElementMatcher<JavaMethodMeta> {

    /**
     * @key 类对象
     * @value 方法名称列表
     */
    private final Map<Class<?>, List<String>> ignoreMethodNames;

    public JavaMethodMatcher(Map<Class<?>, List<String>> ignoreMethodNames) {
        this.ignoreMethodNames = ignoreMethodNames;
    }

    @Override
    public boolean matches(JavaMethodMeta javaMethodMeta) {
        Method method = javaMethodMeta.getMethod();
        if (method == null) {
            return true;
        }
        if (isStaticOrNative(javaMethodMeta)){
            return false;
        }
        return isMatch(method, ignoreMethodNames.getOrDefault(method.getDeclaringClass(), Collections.emptyList()));
    }

    private boolean isStaticOrNative(JavaMethodMeta javaFieldMeta) {
        return Boolean.TRUE.equals(javaFieldMeta.getIsStatic()) || Boolean.TRUE.equals(javaFieldMeta.getIsNative());
    }

    private boolean isMatch(Method method, List<String> ignoreNames) {
        return !ignoreNames.contains(method.getName());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaMethodMeta.class);
    }
}
