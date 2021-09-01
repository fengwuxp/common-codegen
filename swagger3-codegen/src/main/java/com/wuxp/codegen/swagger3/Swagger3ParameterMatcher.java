package com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaParameterMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Optional;

public class Swagger3ParameterMatcher implements CodeGenElementMatcher<JavaParameterMeta> {

    @Override
    public boolean matches(JavaParameterMeta parameterMeta) {
        Parameter parameter = parameterMeta.getParameter();
        Schema schema = parameter.getAnnotation(Schema.class);
        if (schema != null) {
            return !schema.hidden();
        }
        Method method = (Method) parameter.getDeclaringExecutable();
        String parameterName = parameter.getName();
        Optional<Parameters> parameters = Optional.ofNullable(method.getAnnotation(Parameters.class));
        return parameters.map(value -> Arrays.stream(value.value())
                        .filter(item -> item.name().equals(parameterName))
                        .findFirst())
                .orElseGet(() -> Optional.ofNullable(method.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class)))
                .map(io.swagger.v3.oas.annotations.Parameter::hidden)
                .orElse(true);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaParameterMeta.class);
    }
}
