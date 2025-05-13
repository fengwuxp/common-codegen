package com.wuxp.codegen.languages;

import com.google.common.collect.ImmutableSet;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import com.wuxp.codegen.meta.util.SpringControllerFilterUtils;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.wuxp.codegen.model.constant.SpringAnnotationClassConstant.SPRING_MAPPING_ANNOTATIONS;

/**
 * 在接口注释中增加 request destination
 *
 * @author wuxp
 * @date 2024-05-27 16:31
 **/
public class HttpRequestDestinationPostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {

    private static final Set<Class<? extends Annotation>> MAPPING_ANNOTATIONS = ImmutableSet.copyOf(
            SPRING_MAPPING_ANNOTATIONS
    );

    private static final RequestMappingMetaFactory REQUEST_MAPPING_PROCESSOR = new RequestMappingMetaFactory();

    @Override
    public void postProcess(CommonCodeGenMethodMeta meta) {
        Class<?> controllerClass = meta.getSource().getDeclaringClass();
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }
        Assert.notEmpty(requestMapping.value(), () -> "RequestMapping value must not empty, class: " + controllerClass.getName());
        CommonCodeGenAnnotation methodMapping = Arrays.stream(meta.getAnnotations())
                .filter(annotation -> MAPPING_ANNOTATIONS.contains(annotation.getSource().annotationType()))
                .findFirst()
                .orElseThrow(() -> new CodegenRuntimeException("Not found Spring Mapping Annotation: " + meta.getSource().getName()));

        RequestMappingMetaFactory.RequestMappingMate mappingMate = REQUEST_MAPPING_PROCESSOR.factory(methodMapping.getSource());

        List<String> comments = new ArrayList<>(Arrays.asList(meta.getComments()));
        comments.add(0, String.format("%s %s", mappingMate.getRequestMethod(), RequestMappingUtils.combinePath(mappingMate, meta.getSource())));
        meta.setComments(comments.toArray(new String[0]));

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenMethodMeta.class);
    }
}
