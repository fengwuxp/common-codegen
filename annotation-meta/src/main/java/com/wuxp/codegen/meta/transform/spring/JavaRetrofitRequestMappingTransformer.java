package com.wuxp.codegen.meta.transform.spring;

import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.meta.transform.AnnotationCodeGenTransformer;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
        AnnotationCodeGenTransformer<CommonCodeGenAnnotation, RequestMappingMetaFactory.RequestMappingMate> {

    /**
     * 请求方法和Mapping名称的对应
     */
    private static final Map<RequestMethod, String> METHOD_MAPPING_NAME_MAP = new EnumMap<>(RequestMethod.class);


    static {

        METHOD_MAPPING_NAME_MAP.put(RequestMethod.GET, "GET");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.POST, "POST");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.DELETE, "DELETED");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PUT, "PUT");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PATCH, "PATCH");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.HEAD, "HEAD");

    }


    @Override
    public CommonCodeGenAnnotation transform(RequestMappingMetaFactory.RequestMappingMate annotationMate, Method annotationOwner) {

        CommonCodeGenAnnotation codeGenAnnotation = new CommonCodeGenAnnotation();
        //注解命名参数
        Map<String, String> namedArguments = new LinkedHashMap<>();

        String value = RequestMappingUtils.combinePath(annotationMate, annotationOwner);
        if (value != null) {
            namedArguments.put("value", MessageFormat.format("\"{0}\"", value));
        } else {
            log.warn("类：{}的方法：{}合并的url为null，请检查控制的注解", annotationOwner.getDeclaringClass().getName(), annotationOwner.getName());
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

        codeGenAnnotation.setNamedArguments(namedArguments);
        //注解位置参数
        List<String> positionArguments = new LinkedList<>();
        positionArguments.add(namedArguments.get("value"));
        codeGenAnnotation.setPositionArguments(positionArguments);
        String[] produces = annotationMate.produces();
        if (produces.length > 0) {
            List<CommonCodeGenAnnotation> associatedAnnotations = new ArrayList<>();
            associatedAnnotations.add(this.getHeaders(annotationMate));
            codeGenAnnotation.setAssociatedAnnotations(associatedAnnotations);
        }

        return codeGenAnnotation;
    }

    private CommonCodeGenAnnotation getHeaders(RequestMappingMetaFactory.RequestMappingMate annotationMate) {
        CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
        annotation.setName("Headers");
        //注解命名参数
        String[] produces = annotationMate.produces();
        Map<String, String> namedArguments = new LinkedHashMap<>();
        namedArguments.put("value", "{\"" + HttpHeaders.CONTENT_TYPE + ": " + produces[0] + "\"}");
        annotation.setNamedArguments(namedArguments);
        return annotation;
    }


}
