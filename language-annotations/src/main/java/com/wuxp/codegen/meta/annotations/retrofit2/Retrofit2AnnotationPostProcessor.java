package com.wuxp.codegen.meta.annotations.retrofit2;

import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.wuxp.codegen.meta.annotations.factories.AnnotationMate.ANNOTATION_VALUE_KEY;

/**
 * @author wuxp
 * @date 2024-02-27 18:20
 **/
public class Retrofit2AnnotationPostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {

    @Override
    public void postProcess(CommonCodeGenMethodMeta meta) {
        Method method = meta.getSource();
        RequestMappingUtils.findRequestMappingAnnotation(method.getAnnotations()).ifPresent(requestMappingMeta -> {
            if (Arrays.asList(RequestMethod.DELETE, RequestMethod.GET).contains(requestMappingMeta.getRequestMethod())) {
                Map<String, CommonCodeGenAnnotation[]> paramAnnotations = meta.getParamAnnotations();
                meta.getParams().forEach((name, v) -> {
                    CommonCodeGenAnnotation[] annotations = paramAnnotations.getOrDefault(name, new CommonCodeGenAnnotation[0]);
                    List<CommonCodeGenAnnotation> result = new ArrayList<>(Arrays.asList(annotations));
                    CommonCodeGenAnnotation queryAnnotation = genQueryAnnotation(finParameter(name, method.getParameters()));
                    if (queryAnnotation != null) {
                        result.add(queryAnnotation);
                    }
                    paramAnnotations.put(name, result.toArray(new CommonCodeGenAnnotation[0]));
                });
            }
        });
    }

    private Parameter finParameter(String name, Parameter[] parameters) {
        return Arrays.stream(parameters).
                filter(parameter -> Objects.equals(name, parameter.getName()))
                .findFirst()
                .orElseThrow(() -> new CodegenRuntimeException(String.format("find name = %sparameter error", name)));
    }

    private CommonCodeGenAnnotation genQueryAnnotation(Parameter parameter) {
        if (parameter.isAnnotationPresent(RequestParam.class)) {
            // 有 RequestParam 注解
            return null;
        }
        CommonCodeGenAnnotation result = new CommonCodeGenAnnotation();
        Map<String, String> namedArguments = new HashMap<>();
        Type parameterizedType = parameter.getParameterizedType();
        if (parameterizedType instanceof Class && (JavaTypeUtils.isMap((Class<?>) parameterizedType) ||
                JavaTypeUtils.isNoneJdkComplex((Class<?>) parameterizedType))) {
            // 复杂对象
            result.setName(QueryMap.class.getSimpleName());
        } else {
            // 没有注解则使用参数名称
            result.setName(Query.class.getSimpleName());
            namedArguments.put(ANNOTATION_VALUE_KEY, "\"" + parameter.getName() + "\"");
        }
        result.setNamedArguments(namedArguments);
        result.setElementType(ElementType.PARAMETER);
        return result;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenMethodMeta.class);
    }
}
