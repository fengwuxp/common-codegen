package com.wuxp.codegen.transform.spring;

import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.transform.AnnotationCodeGenTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;


/**
 * 将spring requestMapping相关的注解转换为 retrofit相关的注解
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
        String uri1 = v1.length > 0 ? v1[0] : "";
        String uri2 = v2.length > 0 ? v2[0] : "";

        String value = uri1.startsWith("/") ? uri1.substring(1, uri1.length()) : uri1;

        if (value.endsWith("/")) {
            if (uri2.startsWith("/")) {
                uri2 = uri2.substring(1, uri1.length());
            }
            value = MessageFormat.format("{0}{1}", value, uri2);
        } else {
            if (!uri2.startsWith("/")) {
                uri2 = MessageFormat.format("/{0}", uri2);
            }
            value = MessageFormat.format("{0}{1}", value, uri2);
        }

        arguments.put("value", MessageFormat.format("\"{0}\"", value));

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

}
