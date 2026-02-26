package com.wuxp.codegen.meta.transform.spring;

import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.meta.transform.AnnotationCodeGenTransformer;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.constant.MappingAnnotationPropNameConstant;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.wuxp.codegen.core.parser.JavaClassParser.JAVA_CLASS_PARSER;

/**
 * @author wuxp
 * @see RequestMapping
 */
public class SpringRequestMappingTransformer implements
        AnnotationCodeGenTransformer<CommonCodeGenAnnotation, RequestMappingMetaFactory.RequestMappingMate> {

    public static final String SPRING_OPENFEIGN_CLIENT_ANNOTATION_NAME = "FeignClient";

    /**
     * Convention over Configuration（约定优于配置）
     */
    public static final AtomicBoolean USE_COC = new AtomicBoolean(false);

    /**
     * 请求方法和Mapping名称的对应
     */
    protected static final Map<RequestMethod, String> METHOD_MAPPING_NAME_MAP = new EnumMap<>(RequestMethod.class);

    /**
     * 媒体类型映射
     */
    protected static final Map<String, String> MEDIA_TYPE_MAPPING = new LinkedHashMap<>();

    static {
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.GET, GetMapping.class.getSimpleName());
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.POST, PostMapping.class.getSimpleName());
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.DELETE, DeleteMapping.class.getSimpleName());
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PUT, PutMapping.class.getSimpleName());
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PATCH, PatchMapping.class.getSimpleName());

        MEDIA_TYPE_MAPPING.put(MediaType.MULTIPART_FORM_DATA_VALUE, "MediaType.MULTIPART_FORM_DATA_VALUE");
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_FORM_URLENCODED_VALUE, "MediaType.APPLICATION_FORM_URLENCODED_VALUE");
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_JSON_VALUE, "MediaType.APPLICATION_JSON_VALUE");
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_JSON_UTF8_VALUE, "MediaType.APPLICATION_JSON_UTF8_VALUE");
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_OCTET_STREAM_VALUE, "MediaType.APPLICATION_OCTET_STREAM_VALUE");
    }

    @Override
    public CommonCodeGenAnnotation transform(RequestMappingMetaFactory.RequestMappingMate annotationMate, Class<?> annotationOwner) {
        CommonCodeGenAnnotation codeGenAnnotation = this.innerTransform(annotationMate, annotationOwner.getSimpleName());
        codeGenAnnotation.setName(SPRING_OPENFEIGN_CLIENT_ANNOTATION_NAME);
        Map<String, String> namedArguments = codeGenAnnotation.getNamedArguments();
        String value = namedArguments.remove("value");
        if (value != null) {
            namedArguments.put("path", value);
        }
        return codeGenAnnotation;
    }

    @Override
    public CommonCodeGenAnnotation transform(RequestMappingMetaFactory.RequestMappingMate annotationMate, Method annotationOwner) {
        CommonCodeGenAnnotation commonCodeGenAnnotation = this.innerTransform(annotationMate, annotationOwner.getName());
        Map<String, String> namedArguments = commonCodeGenAnnotation.getNamedArguments();
        String consumes = namedArguments.get(MappingAnnotationPropNameConstant.CONSUMES);
        if (consumes != null) {
            return commonCodeGenAnnotation;
        }
        JavaMethodMeta javaMethodMeta = JAVA_CLASS_PARSER.getJavaMethodMeta(annotationOwner);
        boolean isNoneReturnFile = Arrays.stream(javaMethodMeta.getReturnType()).noneMatch(clazz -> JavaTypeUtils.isAssignableFrom(clazz,
                InputStreamResource.class));
        if (isNoneReturnFile) {
            return commonCodeGenAnnotation;
        }
        // 返回文件类型的数据从新设置consumes
        List<String> positionArguments = commonCodeGenAnnotation.getPositionArguments();
        String fileConsumes = wrapperArraySymbol(MEDIA_TYPE_MAPPING.get(MediaType.APPLICATION_OCTET_STREAM_VALUE));
        positionArguments.add(fileConsumes);
        namedArguments.put(MappingAnnotationPropNameConstant.CONSUMES, fileConsumes);
        return commonCodeGenAnnotation;
    }

    /**
     * 获取数组符号
     *
     * @return 不同语言的数组符号
     */
    protected String[] getArraySymbol() {
        return new String[]{"{", "}"};
    }

    protected CommonCodeGenAnnotation innerTransform(RequestMappingMetaFactory.RequestMappingMate annotationMate, String ownerName) {
        CommonCodeGenAnnotation codeGenAnnotation = new CommonCodeGenAnnotation();
        codeGenAnnotation.setName(annotationMate.annotationType().getSimpleName());

        //注解命名参数
        Map<String, String> namedArguments = new LinkedHashMap<>();
        String[] paths = annotationMate.getPath();
        String val = null;
        if (paths.length > 0) {
            val = paths[0];
        }
        if (USE_COC.get()) {
            //  coc
            if (!StringUtils.hasText(val)) {
                val = ownerName;
            }
            namedArguments.put(MappingAnnotationPropNameConstant.VALUE, "\"" + val + "\"");
        } else {
            //如果 val 不存在 或者 ownerName 和 value 中的一致，则不生成 value
            if (StringUtils.hasText(val) && !ownerName.equals(val)) {
                namedArguments.put(MappingAnnotationPropNameConstant.VALUE, "\"" + val + "\"");
            }
        }

        if (annotationMate.annotationType().equals(RequestMapping.class)) {
            // 将RequestMapping 转换为其他明确的Mapping类型
            RequestMethod requestMethod = annotationMate.getRequestMethod();
            if (requestMethod == null) {
                //默认的为GET
                requestMethod = RequestMethod.GET;
            }
            String name = METHOD_MAPPING_NAME_MAP.get(requestMethod);
            codeGenAnnotation.setName(name);
        }

        Map<String, String[]> mediaTypes = new HashMap<>();
        // 在注解中属性名称
        String[] attrNames = {MappingAnnotationPropNameConstant.PRODUCES, MappingAnnotationPropNameConstant.CONSUMES};
        // 客户端和服务的 produces consumes 逻辑对调
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

            String targetMediaType = MEDIA_TYPE_MAPPING.get(mediaType);
            if (targetMediaType == null) {
                throw new CodegenRuntimeException("unsupported media type：" + mediaType);
            }
            namedArguments.put(entry.getKey(), wrapperArraySymbol(targetMediaType));
        }

        codeGenAnnotation.setNamedArguments(namedArguments);
        //注解位置参数
        List<String> positionArguments = new LinkedList<>();
        positionArguments.add(namedArguments.get(MappingAnnotationPropNameConstant.VALUE));
        positionArguments.add(namedArguments.get(MappingAnnotationPropNameConstant.METHOD));
        if (!annotationMate.isGetMethod()) {
            // GET 请求不需要produces
            positionArguments.add(namedArguments.get(MappingAnnotationPropNameConstant.PRODUCES));
        }
        positionArguments.add(namedArguments.get(MappingAnnotationPropNameConstant.CONSUMES));
        codeGenAnnotation.setPositionArguments(positionArguments);

        return codeGenAnnotation;
    }

    private String wrapperArraySymbol(String targetMediaType) {
        String[] arraySymbol = getArraySymbol();
        //是否已经包装了数组符号
        boolean isWrapperJsonArray = targetMediaType.startsWith(arraySymbol[0]) && targetMediaType.endsWith(arraySymbol[1]);
        if (!isWrapperJsonArray) {
            targetMediaType = arraySymbol[0] + targetMediaType + arraySymbol[1];
        }
        return targetMediaType;
    }

}
