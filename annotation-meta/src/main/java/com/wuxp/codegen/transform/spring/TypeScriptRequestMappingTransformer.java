package com.wuxp.codegen.transform.spring;


import com.wuxp.codegen.annotation.processors.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.constant.TypescriptFeignMediaTypeConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wxup
 * @see RequestMapping
 */
@Slf4j
public class TypeScriptRequestMappingTransformer extends SpringRequestMappingTransformer {

    public static final String TS_FEIGN_CLIENT_ANNOTATION_NAME = "Feign";

    {
        mediaTypeMapping.put(MediaType.MULTIPART_FORM_DATA_VALUE, TypescriptFeignMediaTypeConstant.MULTIPART_FORM_DATA);
        mediaTypeMapping.put(MediaType.APPLICATION_FORM_URLENCODED_VALUE, TypescriptFeignMediaTypeConstant.FORM_DATA);
        mediaTypeMapping.put(MediaType.APPLICATION_JSON_VALUE, TypescriptFeignMediaTypeConstant.APPLICATION_JSON);
        mediaTypeMapping.put(MediaType.APPLICATION_JSON_UTF8_VALUE, TypescriptFeignMediaTypeConstant.APPLICATION_JSON_UTF8);
        mediaTypeMapping.put(MediaType.APPLICATION_OCTET_STREAM_VALUE, TypescriptFeignMediaTypeConstant.APPLICATION_OCTET_STREAM);
    }


    @Override
    public CommonCodeGenAnnotation transform(RequestMappingMetaFactory.RequestMappingMate annotationMate, Class<?> annotationOwner) {
        CommonCodeGenAnnotation annotation = super.innerTransform(annotationMate, annotationOwner.getSimpleName());
        annotation.setName(TS_FEIGN_CLIENT_ANNOTATION_NAME);
        return annotation;
    }


    @Override
    protected String[] getArraySymbol() {
        return new String[]{"[", "]"};
    }
}