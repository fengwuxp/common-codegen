package com.wuxp.codegen.transform.spring;


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

    {
        mediaTypeMapping.put(MediaType.MULTIPART_FORM_DATA_VALUE, TypescriptFeignMediaTypeConstant.MULTIPART_FORM_DATA);
        mediaTypeMapping.put(MediaType.APPLICATION_FORM_URLENCODED_VALUE, TypescriptFeignMediaTypeConstant.FORM_DATA);
        mediaTypeMapping.put(MediaType.APPLICATION_JSON_VALUE, TypescriptFeignMediaTypeConstant.APPLICATION_JSON);
        mediaTypeMapping.put(MediaType.APPLICATION_JSON_UTF8_VALUE, TypescriptFeignMediaTypeConstant.APPLICATION_JSON_UTF8);
    }

    @Override
    protected String[] getArraySymbol() {
        return new String[]{"[", "]"};
    }
}