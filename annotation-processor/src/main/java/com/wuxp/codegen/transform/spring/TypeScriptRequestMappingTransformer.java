package com.wuxp.codegen.transform.spring;


import com.alibaba.fastjson.JSON;
import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.transform.AnnotationCodeGenTransformer;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.*;

/**
 * @see RequestMapping
 */
@Slf4j
public class TypeScriptRequestMappingTransformer implements
        AnnotationCodeGenTransformer<CommonCodeGenAnnotation, Annotation> {

    private RequestMappingProcessor requestMappingProcessor = new RequestMappingProcessor();

    private static final Map<RequestMethod, String> METHOD_NAME_MAP = new LinkedHashMap();

    static {
        METHOD_NAME_MAP.put(RequestMethod.GET, "GetMapping");
        METHOD_NAME_MAP.put(RequestMethod.POST, "PostMapping");
        METHOD_NAME_MAP.put(RequestMethod.DELETE, "DeleteMapping");
        METHOD_NAME_MAP.put(RequestMethod.PUT, "PutMapping");
    }

    /**
     * @param annotations 注解列表
     * @return
     */
    @Override
    public CommonCodeGenAnnotation transform(Annotation[] annotations) {

        Optional<RequestMappingProcessor.RequestMappingMate> mateOptional = Arrays.stream(annotations)
                .map(annotation -> this.requestMappingProcessor.process(annotation)).
                        filter(Objects::nonNull).findFirst();
        if (!mateOptional.isPresent()) {
            log.debug("未找到RequestMapping相关的注解");
            return null;
        }
        RequestMappingProcessor.RequestMappingMate mappingMate = mateOptional.get();

        CommonCodeGenAnnotation codeGenAnnotation = new CommonCodeGenAnnotation();

        RequestMethod[] requestMethods = mappingMate.method();

        codeGenAnnotation.setName("@" + METHOD_NAME_MAP.get(requestMethods.length == 0 ? RequestMethod.POST : requestMethods[0].name()));

        Map<String, String> params = new LinkedHashMap<>();

        String requestPath = this.requestPath(mappingMate);
        if (requestPath != null) {
            params.put("value", "'" + requestPath + "'");
        }
        //处理请求头
        handleRequestHeader(annotations, params);

        //TODO 加入 consumes 和 produces的处理
        codeGenAnnotation.setNamedArguments(params);

        return codeGenAnnotation;
    }

    /**
     * 处理请求头信息
     *
     * @param annotations
     * @param params
     */
    private void handleRequestHeader(Annotation[] annotations, Map<String, String> params) {

        Optional<RequestHeader> requestHeaderOptional = Arrays.stream(annotations)
                .filter(annotation -> RequestHeader.class.equals(annotation.getClass()))
                .map(annotation -> (RequestHeader) annotation)
                .findFirst();
        if (!requestHeaderOptional.isPresent()) {
            return;
        }
        RequestHeader requestHeader = requestHeaderOptional.get();
        Map<String, String> map = new HashMap<>();
        String value = requestHeader.value();
        if (!StringUtils.hasText(value)) {
            value = requestHeader.name();
        }
        map.put(value, MessageFormat.format("'{'{0}'}'", value));
        params.put("headers", JSON.toJSONString(map));
    }


    /**
     * 获取请求uri
     *
     * @param mappingMate
     * @return
     */
    private String requestPath(RequestMappingProcessor.RequestMappingMate mappingMate) {
        if (StringUtils.hasText(mappingMate.name())) {
            return mappingMate.name();
        }
        String[] value = mappingMate.value();
        if (value.length != 0) {
            return value[0];
        }
        String[] path = mappingMate.path();
        if (path.length != 0) {
            return path[0];
        }
        return null;
    }

    /**
     * 是否为post请求
     *
     * @param mappingMate
     * @return
     */
    private boolean methodIsPost(RequestMappingProcessor.RequestMappingMate mappingMate) {
        return RequestMethod.POST.equals(mappingMate.method()[0]);
    }


}
