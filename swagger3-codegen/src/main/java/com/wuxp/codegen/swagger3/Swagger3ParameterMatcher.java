package com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaParameterMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class Swagger3ParameterMatcher implements CodeGenElementMatcher<JavaParameterMeta> {

    @Override
    public boolean matches(JavaParameterMeta parameterMeta) {
        Parameter parameter = parameterMeta.getParameter();
        Schema schema = parameter.getAnnotation(Schema.class);
        if (schema != null) {
            return !schema.hidden();
        }
        io.swagger.v3.oas.annotations.Parameter parameterAnnotation = parameter.getAnnotation(io.swagger.v3.oas.annotations.Parameter.class);
        if (parameterAnnotation != null) {
            return !parameterAnnotation.hidden();
        }
        Method method = (Method) parameter.getDeclaringExecutable();
        String parameterName = parameter.getName();
        Parameters parameters = method.getAnnotation(Parameters.class);
        if (parameters == null) {
            return true;
        }
        return Arrays.stream(parameters.value())
                .filter(item -> item.name().equals(parameterName))
                .findFirst()
                .map(p -> !p.hidden())
                .orElse(true);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaParameterMeta.class);
    }
}
