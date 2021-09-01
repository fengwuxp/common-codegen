package com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import io.swagger.annotations.ApiModelProperty;

public class Swagger2FieldMatcher implements CodeGenElementMatcher<JavaFieldMeta> {

    @Override
    public boolean matches(JavaFieldMeta javaFieldMeta) {
        return javaFieldMeta.getAnnotation(ApiModelProperty.class)
                .map(value -> !value.hidden())
                .orElse(true);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaFieldMeta.class);
    }
}
