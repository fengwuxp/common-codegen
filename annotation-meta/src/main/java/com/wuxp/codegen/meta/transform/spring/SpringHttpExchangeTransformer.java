package com.wuxp.codegen.meta.transform.spring;

import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * http exchange 转换
 *
 * @author wuxp
 * @date 2026-02-26 14:11
 **/
public class SpringHttpExchangeTransformer extends SpringRequestMappingTransformer {

    private static final Map<String, String> METHOD_ANNOTATION_NAMES = new ConcurrentHashMap<>();

    static {
        METHOD_ANNOTATION_NAMES.put(GetMapping.class.getSimpleName(), GetExchange.class.getSimpleName());
        METHOD_ANNOTATION_NAMES.put(PostMapping.class.getSimpleName(), PostExchange.class.getSimpleName());
        METHOD_ANNOTATION_NAMES.put(DeleteMapping.class.getSimpleName(), DeleteExchange.class.getSimpleName());
        METHOD_ANNOTATION_NAMES.put(PutMapping.class.getSimpleName(), PutExchange.class.getSimpleName());
        METHOD_ANNOTATION_NAMES.put(PatchMapping.class.getSimpleName(), PatchExchange.class.getSimpleName());
    }

    @Override
    public CommonCodeGenAnnotation transform(RequestMappingMetaFactory.RequestMappingMate annotationMate, Class<?> annotationOwner) {
        CommonCodeGenAnnotation result = super.transform(annotationMate, annotationOwner);
        Map<String, String> namedArguments = result.getNamedArguments();
        String path = namedArguments.remove("path");
        if (path != null) {
            namedArguments.put("value", path);
        }
        result.setName(HttpExchange.class.getSimpleName());
        return result;
    }

    @Override
    public CommonCodeGenAnnotation transform(RequestMappingMetaFactory.RequestMappingMate annotationMate, Method annotationOwner) {
        CommonCodeGenAnnotation result = super.transform(annotationMate, annotationOwner);
        Map<String, String> namedArguments = result.getNamedArguments();
        String consumes = namedArguments.remove("consumes");
        if (consumes != null) {
            namedArguments.put("contentType", consumes);
        }
        String produces = namedArguments.remove("produces");
        if (produces != null) {
            namedArguments.put("accept", produces);
        }
        // 映射注解名称
        result.setName(METHOD_ANNOTATION_NAMES.get(result.getName()));
        return result;
    }

}
