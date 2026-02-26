package com.wuxp.codegen.meta.transform.spring;


import com.alibaba.fastjson2.JSON;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.constant.TypescriptFeignMediaTypeConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wxup
 * @see RequestMapping
 */
@Slf4j
public class TypeScriptRequestMappingTransformer extends SpringRequestMappingTransformer {

    public static final String TS_FEIGN_CLIENT_ANNOTATION_NAME = "Feign";

    {
        MEDIA_TYPE_MAPPING.put(MediaType.MULTIPART_FORM_DATA_VALUE, TypescriptFeignMediaTypeConstant.MULTIPART_FORM_DATA);
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_FORM_URLENCODED_VALUE, TypescriptFeignMediaTypeConstant.FORM_DATA);
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_JSON_VALUE, TypescriptFeignMediaTypeConstant.APPLICATION_JSON);
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_OCTET_STREAM_VALUE, TypescriptFeignMediaTypeConstant.APPLICATION_OCTET_STREAM);
    }


    @Override
    public CommonCodeGenAnnotation transform(RequestMappingMetaFactory.RequestMappingMate annotationMate, Class<?> annotationOwner) {
        CommonCodeGenAnnotation annotation = super.innerTransform(annotationMate, annotationOwner.getSimpleName());
        annotation.setName(TS_FEIGN_CLIENT_ANNOTATION_NAME);
        return annotation;
    }

    @Override
    public CommonCodeGenAnnotation transform(RequestMappingMetaFactory.RequestMappingMate annotationMate, Method annotationOwner) {
        CommonCodeGenAnnotation result = super.transform(annotationMate, annotationOwner);
        Map<String, String> headers = new HashMap<>();
        String[] parameterNames = JavaClassParser.PARAMETER_NAME_DISCOVERER.getParameterNames(annotationOwner);
        if (ObjectUtils.isEmpty(parameterNames)) {
            return result;
        }
        int i = 0;
        for (Parameter parameter : annotationOwner.getParameters()) {
            RequestHeader header = parameter.getAnnotation(RequestHeader.class);
            if (header != null) {
                String name = StringUtils.hasText(header.value()) ? header.value() : header.name();
                Assert.hasText(name, () -> String.format("method: %s#%s RequestHeader annotation value or name is empty", annotationOwner.getDeclaringClass().getName(), annotationOwner.getName()));
                headers.put(name, "{" + parameterNames[i] + "}");
            }
            i++;
        }
        if (!headers.isEmpty()) {
            Map<String, String> namedArguments = result.getNamedArguments();
            namedArguments.put("headers", JSON.toJSONString(headers));
        }
        return result;
    }


    @Override
    protected String[] getArraySymbol() {
        return new String[]{"[", "]"};
    }
}