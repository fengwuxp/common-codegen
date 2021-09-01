package com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Optional;

public class Swagger3MethodMatcher implements CodeGenElementMatcher<JavaMethodMeta> {

    @Override
    public boolean matches(JavaMethodMeta methodMeta) {
        if (methodMeta.existAnnotation(Hidden.class)) {
            return false;
        }
        Optional<Operation> operation = methodMeta.getAnnotation(Operation.class);
        return operation.map(value -> !value.hidden()).orElse(true);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaMethodMeta.class);
    }
}
