package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 字段是否支持生成
 */
public class JavaFieldMatcher implements CodeGenElementMatcher<JavaFieldMeta> {

    /**
     * @key 类对象
     * @value 字段名称列表
     */
    private final Map<Class<?>, List<String>> ignoreFieldNames;

    public JavaFieldMatcher(Map<Class<?>, List<String>> ignoreFieldNames) {
        this.ignoreFieldNames = ignoreFieldNames;
    }

    @Override
    public boolean matches(JavaFieldMeta javaFieldMeta) {
        Field field = javaFieldMeta.getField();
        if (field == null) {
            return true;
        }
        if (isStaticOrTransient(javaFieldMeta)) {
            return false;
        }
        return isMatch(field, ignoreFieldNames.getOrDefault(field.getDeclaringClass(), Collections.emptyList()));
    }

    private boolean isStaticOrTransient(JavaFieldMeta javaFieldMeta) {
        Class<?> declaringClass = javaFieldMeta.getField().getDeclaringClass();
        if (!declaringClass.isEnum() && Boolean.TRUE.equals(javaFieldMeta.getIsStatic())) {
            return true;
        }
        return Boolean.TRUE.equals(javaFieldMeta.getIsTransient());
    }

    private boolean isMatch(Field field, List<String> ignoreNames) {
        return !ignoreNames.contains(field.getName());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaFieldMeta.class);
    }
}
