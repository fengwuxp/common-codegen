package com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

public class Swagger2MethodMatcher implements CodeGenElementMatcher<JavaMethodMeta> {

    @Override
    public boolean matches(JavaMethodMeta methodMeta) {
        if (methodMeta.existAnnotation(ApiIgnore.class)) {
            return false;
        }
        Optional<ApiOperation> annotation = methodMeta.getAnnotation(ApiOperation.class);
        return annotation.map(value -> !value.hidden()).orElse(true);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaMethodMeta.class);
    }
}
