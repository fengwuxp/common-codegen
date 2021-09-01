package com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import io.swagger.v3.oas.annotations.media.Schema;

public class Swagger3FieldMatcher implements CodeGenElementMatcher<JavaFieldMeta> {

    @Override
    public boolean matches(JavaFieldMeta javaFieldMeta) {
        return javaFieldMeta.getAnnotation(Schema.class)
                .map(value -> !value.hidden())
                .orElse(true);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaFieldMeta.class);
    }
}
