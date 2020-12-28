package com.wuxp.codegen.transform.spring;

import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.transform.AnnotationCodeGenTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;


/**
 * 将spring requestMapping相关的注解转换为 retrofit相关的注解
 *
 * @author wuxp
 */
@Slf4j
public class JavaRetrofitRequestMappingTransformer implements
        AnnotationCodeGenTransformer<CommonCodeGenAnnotation, RequestMappingProcessor.RequestMappingMate> {

    /**
     * 请求方法和Mapping名称的对应
     */
    private static final Map<RequestMethod, String> METHOD_MAPPING_NAME_MAP = new HashMap<>();

    private final RequestMappingProcessor requestMappingProcessor = new RequestMappingProcessor();

    private final PathMatcher pathMatcher = new AntPathMatcher();

    static {

        METHOD_MAPPING_NAME_MAP.put(RequestMethod.GET, "GET");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.POST, "POST");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.DELETE, "DELETED");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PUT, "PUT");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PATCH, "PATCH");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.HEAD, "HEAD");

    }


    @Override
    public CommonCodeGenAnnotation transform(RequestMappingProcessor.RequestMappingMate annotationMate, Method annotationOwner) {

        CommonCodeGenAnnotation codeGenAnnotation = new CommonCodeGenAnnotation();
        //注解命名参数
        Map<String, String> arguments = new LinkedHashMap<>();

        Class<?> declaringClass = annotationOwner.getDeclaringClass();

        Annotation[] annotations = declaringClass.getAnnotations();

        Optional<RequestMappingProcessor.RequestMappingMate> mappingMate = Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType().getSimpleName().endsWith("Mapping"))
                .map(requestMappingProcessor::process)
                .filter(Objects::nonNull)
                .findFirst();

        if (!mappingMate.isPresent()) {
            return null;
        }
        RequestMappingProcessor.RequestMappingMate requestMappingMate = mappingMate.get();

        String[] v1 = requestMappingMate.value();
        String[] v2 = annotationMate.value();
        String value = combinePath(v1, v2);
        if (value != null) {
            arguments.put("value", MessageFormat.format("\"{0}\"", value));
        } else {
            log.warn("类：{}的方法：{}合并的url为null，请检查控制的注解", declaringClass.getName(), annotationOwner.getName());
        }
        RequestMethod requestMethod = annotationMate.getRequestMethod();
        if (annotationMate.annotationType().equals(RequestMapping.class)) {
            //将RequestMapping 转换为其他明确的Mapping类型
            if (requestMethod == null) {
                //默认的为GET
                requestMethod = RequestMethod.GET;
            }
        }
        String name = METHOD_MAPPING_NAME_MAP.get(requestMethod);
        codeGenAnnotation.setName(name);

        codeGenAnnotation.setNamedArguments(arguments);
        //注解位置参数
        List<String> positionArguments = new LinkedList<>();
        positionArguments.add(arguments.get("value"));
        codeGenAnnotation.setPositionArguments(positionArguments);

        return codeGenAnnotation;
    }

    private String combinePath(String[] patterns, String[] otherPatterns) {
        if (patterns.length == 0) {
            return null;
        }
        if (otherPatterns.length == 0) {
            return patterns[0];
        }
        for (String pattern1 : patterns) {
            for (String pattern2 : otherPatterns) {
                return this.pathMatcher.combine(pattern1, pattern2);
            }
        }
        return null;
    }

}
