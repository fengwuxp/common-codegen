package com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.model.languages.java.JavaParameterMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;

import java.lang.reflect.Parameter;
import java.util.Arrays;

public class Swagger2ParameterMatcher implements CodeGenElementMatcher<JavaParameterMeta> {

    @Override
    public boolean matches(JavaParameterMeta parameterMeta) {
        Parameter parameter = parameterMeta.getParameter();
        ApiParam apiParam = parameter.getAnnotation(ApiParam.class);
        if (apiParam != null) {
            return !apiParam.hidden();
        }
        String parameterName = parameter.getName();
        return parameterMeta.getAnnotation(ApiImplicitParams.class)
                .map(apiImplicitParams -> Arrays.stream(apiImplicitParams.value())
                        .filter(item -> item.name().equals(parameterName))
                        .findFirst())
                .orElseGet(() -> parameterMeta.getAnnotation(ApiImplicitParam.class))
                .map(apiImplicitParam -> !apiImplicitParam.readOnly())
                .orElse(true);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, JavaParameterMeta.class);
    }
}
