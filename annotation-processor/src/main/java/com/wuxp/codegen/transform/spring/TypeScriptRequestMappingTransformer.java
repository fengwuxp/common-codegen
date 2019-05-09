package com.wuxp.codegen.transform.spring;


import com.alibaba.fastjson.JSON;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.transform.AnnotationCodeGenTransformer;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;

/**
 * @see RequestMapping
 */
@Slf4j
public class TypeScriptRequestMappingTransformer implements
        AnnotationCodeGenTransformer<CommonCodeGenAnnotation, RequestMappingProcessor.RequestMappingMate> {


    //请求方法和Mapping名称的对应
    private static final Map<RequestMethod, String> METHOD_MAPPING_NAME_MAP = new HashMap<>();


    //媒体类型映射
    private static final Map<String, String> MEDIA_TYPE_MAPPING = new LinkedHashMap<>();




    static {


        METHOD_MAPPING_NAME_MAP.put(RequestMethod.GET, "GetMapping");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.POST, "PostMapping");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.DELETE, "DeleteMapping");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PUT, "PutMapping");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PATCH, "PatchMapping");

        MEDIA_TYPE_MAPPING.put(MediaType.MULTIPART_FORM_DATA_VALUE, "MediaType.FORM_DATA");
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_JSON_VALUE, "MediaType.JSON");
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_JSON_UTF8_VALUE, "MediaType.JSON_UTF8");

    }

    @Override
    public CommonCodeGenAnnotation transform(RequestMappingProcessor.RequestMappingMate annotationMate, Class<?> annotationOwner) {
        return this.innerTransform(annotationMate, annotationOwner.getSimpleName());
    }

    @Override
    public CommonCodeGenAnnotation transform(RequestMappingProcessor.RequestMappingMate annotationMate, Method annotationOwner) {
        return this.innerTransform(annotationMate, annotationOwner.getName());
    }

    private CommonCodeGenAnnotation innerTransform(RequestMappingProcessor.RequestMappingMate annotationMate, String ownerName) {
        CommonCodeGenAnnotation codeGenAnnotation = new CommonCodeGenAnnotation();
        codeGenAnnotation.setName(annotationMate.annotationType().getSimpleName());

        //注解命名参数
        Map<String, String> arguments = new LinkedHashMap<>();
        String[] value = annotationMate.value();
        String val = null;
        if (value.length > 0) {
            val = value[0];
        }

        //如果val不存在或者ownerName和value中的一致，则不生成value
        if (StringUtils.hasText(val) && !ownerName.equals(val)) {
            arguments.put("value", MessageFormat.format("''{0}''", val));
        }
        if (annotationMate.annotationType().equals(RequestMapping.class)) {
//                arguments.put("method", "RequestMethod." + this.getRequestMethod().name());
            //将RequestMapping 转换为其他明确的Mapping类型
            RequestMethod requestMethod = annotationMate.getRequestMethod();
            if (requestMethod == null) {
                //默认的为GET
                requestMethod = RequestMethod.GET;
            }
            String name = METHOD_MAPPING_NAME_MAP.get(requestMethod);
            codeGenAnnotation.setName(name);
        }


        Map<String, String[]> mediaTypes = new HashMap<>();
        //在注解中属性名称
        String[] attrNames = {"produces", "consumes"};
        //客户端和服务的produces consumes 逻辑对调
        mediaTypes.put(attrNames[0], annotationMate.consumes());
        mediaTypes.put(attrNames[1], annotationMate.produces());

        for (Map.Entry<String, String[]> entry : mediaTypes.entrySet()) {
            //尝试转化
            String[] entryValue = entry.getValue();
            if (entryValue.length == 0) {
                continue;
            }
            String mediaType = entryValue[0];

            if (mediaType == null) {
                continue;
            }
            //如果是json 则不生成，json 是默认策略
            if (MediaType.APPLICATION_JSON_VALUE.equals(mediaType)) {
                continue;
            }

            String _mediaType = MEDIA_TYPE_MAPPING.get(mediaType);
            if (_mediaType == null) {
                throw new RuntimeException("unsupported media type：" + mediaType);
            }
            _mediaType = "[" + _mediaType + "]";
            arguments.put(entry.getKey(), _mediaType);
        }


        codeGenAnnotation.setNamedArguments(arguments);

        //注解位置参数
        List<String> positionArguments = new LinkedList<>();
        positionArguments.add(arguments.get("value"));
        positionArguments.add(arguments.get("method"));
        positionArguments.add(arguments.get("produces"));
        positionArguments.add(arguments.get("produces"));

        codeGenAnnotation.setPositionArguments(positionArguments);

        return codeGenAnnotation;
    }

}
