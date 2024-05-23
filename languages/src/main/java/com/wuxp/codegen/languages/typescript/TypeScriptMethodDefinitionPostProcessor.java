package com.wuxp.codegen.languages.typescript;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.ImmutableSet;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wuxp.codegen.languages.AbstractLanguageMethodDefinitionParser.MARGE_PARAMS_TAG_NAME;

/**
 * 处理 typescript-feign 的参数合并，从中标记 reqeustBody 的参数名称
 *
 * @author wuxp
 */
public class TypeScriptMethodDefinitionPostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {

    private static final Set<Class<? extends Annotation>> QUERY_PARAM_ARGS = ImmutableSet.of(
            RequestParam.class
    );

    private static final Set<Class<? extends Annotation>> BODY_ARGS = ImmutableSet.of(
            RequestBody.class
    );

    private static final Set<Class<? extends Annotation>> MAPPING_ANNOTATIONS = ImmutableSet.of(
            RequestMapping.class,
            PostMapping.class,
            PutMapping.class,
            PatchMapping.class,
            GetMapping.class,
            DeleteMapping.class
    );

    private static final Set<Class<? extends Annotation>> SUPPORT_REQUEST_BODY_ANNOTATIONS = ImmutableSet.of(
            PostMapping.class,
            PutMapping.class,
            PatchMapping.class
    );

    @Override
    public void postProcess(CommonCodeGenMethodMeta meta) {
        if (meta.getParams() == null) {
            return;
        }

        if (supportRequestBody(meta)) {
            Set<String> bodyArgNames = getBodyArgNames(meta);
            if (!bodyArgNames.isEmpty()) {
                String bodyArgName = bodyArgNames.toArray(new String[0])[0];
                Arrays.stream(meta.getAnnotations())
                        .filter(annotation -> MAPPING_ANNOTATIONS.contains(annotation.getSource().annotationType()))
                        .forEach(annotation -> annotation.getNamedArguments().put("bodyArgName", JSON.toJSONString(bodyArgName)));
            }
        } else {
            Set<String> queryArgNames = getQueryArgNames(meta);
            if (!queryArgNames.isEmpty()) {
                Arrays.stream(meta.getAnnotations())
                        .filter(annotation -> MAPPING_ANNOTATIONS.contains(annotation.getSource().annotationType()))
                        .forEach(annotation -> annotation.getNamedArguments().put("queryArgNames", JSON.toJSONString(queryArgNames)));
            }
        }
    }

    private Set<String> getQueryArgNames(CommonCodeGenMethodMeta meta) {
        return meta.getParams().values().stream()
                .filter(param -> Boolean.TRUE.equals(param.getTags().get(MARGE_PARAMS_TAG_NAME)))
                .filter(param -> param.getFieldMetas().length > 1)
                .map(param -> {
                    return Arrays.stream(param.getFieldMetas())
                            .filter(TypeScriptMethodDefinitionPostProcessor::isRequestParamArgs)
                            .map(CommonBaseMeta::getName)
                            .collect(Collectors.toSet());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Set<String> getBodyArgNames(CommonCodeGenMethodMeta meta) {
        return meta.getParams().values().stream()
                .filter(param -> Boolean.TRUE.equals(param.getTags().get(MARGE_PARAMS_TAG_NAME)))
                .filter(param -> param.getFieldMetas().length > 1)
                .map(param -> {
                    return Arrays.stream(param.getFieldMetas())
                            .filter(TypeScriptMethodDefinitionPostProcessor::isRequestBodyArgs)
                            .map(CommonBaseMeta::getName)
                            .collect(Collectors.toSet());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private boolean supportRequestBody(CommonCodeGenMethodMeta meta) {
        return Arrays.stream(meta.getAnnotations())
                .anyMatch(annotation -> SUPPORT_REQUEST_BODY_ANNOTATIONS.contains(annotation.getSource().annotationType()));
    }

    private static boolean isRequestBodyArgs(CommonCodeGenFiledMeta field) {
        return Arrays.stream(field.getAnnotations()).anyMatch(annotation -> BODY_ARGS.contains(annotation.getSource().annotationType()));
    }

    private static boolean isRequestParamArgs(CommonCodeGenFiledMeta field) {
        return Arrays.stream(field.getAnnotations()).anyMatch(annotation -> QUERY_PARAM_ARGS.contains(annotation.getSource().annotationType()))
                || (!ObjectUtils.isEmpty(field.getFiledTypes()) && JavaTypeUtils.isNoneJdkComplex(field.getFiledTypes()[0].getSource()));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CommonCodeGenMethodMeta.class.isAssignableFrom(clazz);
    }
}
