package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;

/**
 * @author wuxp
 */
public class CodeGenFieldMatcher implements CodeGenElementMatcher<JavaFieldMeta> {


    @Override
    public boolean matches(JavaFieldMeta javaFieldMeta) {
        if (javaFieldMeta == null) {
            return false;
        }
        Class<?> declaringClass = javaFieldMeta.getField().getDeclaringClass();
        if (!declaringClass.isEnum() && Boolean.TRUE.equals(javaFieldMeta.getIsStatic())) {
            return false;
        }
        if (Boolean.TRUE.equals(javaFieldMeta.getIsTransient())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(JavaFieldMeta.class);
    }
}
