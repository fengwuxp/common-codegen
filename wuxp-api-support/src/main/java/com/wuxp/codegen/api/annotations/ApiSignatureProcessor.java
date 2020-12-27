package com.wuxp.codegen.api.annotations;

import com.alibaba.fastjson.JSON;
import com.wuxp.api.signature.ApiSignature;
import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 处理api signature 注解
 *
 * @author wxup
 * @see ApiSignature
 */
public class ApiSignatureProcessor extends AbstractAnnotationProcessor<ApiSignature, ApiSignatureProcessor.ApiSignatureMate> {

    public static final String ANNOTATION_NAME = "Signature";

    @Override
    public ApiSignatureMate process(ApiSignature annotation) {
        return this.newProxyMate(annotation, ApiSignatureMate.class);
    }

    public abstract static class ApiSignatureMate implements AnnotationMate, ApiSignature {
    }

    public static CommonCodeGenAnnotation genAnnotation(Set<String> needSignFields) {

        CommonCodeGenAnnotation genAnnotation = new CommonCodeGenAnnotation();
        genAnnotation.setName(ANNOTATION_NAME);
        Map<String, String> namedArguments = new LinkedHashMap<>();
        List<String> positionArguments = needSignFields.stream()
                .sorted(Comparator.comparing(Function.identity()))
                .collect(Collectors.toList());
        namedArguments.put("fields", JSON.toJSONString(positionArguments));
        genAnnotation.setNamedArguments(namedArguments);
        genAnnotation.setPositionArguments(positionArguments);

        return genAnnotation;
    }

}
