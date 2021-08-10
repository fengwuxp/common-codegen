package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.*;

import static com.wuxp.codegen.core.parser.JavaClassParser.JAVA_CLASS_ON_PUBLIC_PARSER;

/**
 * 基于umi request的增强配置
 *
 * @author wuxp
 * <a href="https://github.com/umijs/umi-request"></a>
 */
public class UmiRequestMethodDefinitionPostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenMethodMeta> {

    /**
     * umi-request的prefix 配置，在生成是将从url中移除该部分内容
     */
    private final String prefix;

    public UmiRequestMethodDefinitionPostProcessor() {
        this("");
    }

    public UmiRequestMethodDefinitionPostProcessor(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(CommonCodeGenMethodMeta.class);
    }

    @Override
    public void postProcess(CommonCodeGenMethodMeta methodMeta) {
        // delete 是typescript的关键字，不能用作方法名称
        if ("delete".equals(methodMeta.getName())) {
            methodMeta.setName("deleteRequest");
        }

        JavaMethodMeta javaMethodMeta = JAVA_CLASS_ON_PUBLIC_PARSER.getJavaMethodMeta(methodMeta.getSource());
        Optional<RequestMappingMetaFactory.RequestMappingMate> mappingAnnotation = RequestMappingUtils
                .findRequestMappingAnnotation(javaMethodMeta.getAnnotations());
        if (!mappingAnnotation.isPresent()) {
            throw new CodegenRuntimeException(String.format("方法：%s没有RequestMapping相关注解", javaMethodMeta.getName()));
        }
        RequestMappingMetaFactory.RequestMappingMate requestMappingMate = mappingAnnotation.get();
        RequestMethod requestMethod = requestMappingMate.getRequestMethod();
        Map<String, Object> tags = methodMeta.getTags();
        tags.put("httpMethod", requestMethod.name().toLowerCase());
        boolean supportRequestBody = RequestMappingMetaFactory.isSupportRequestBody(requestMethod);
        if (supportRequestBody) {
            tags.put("requestType", this.getRequestType(javaMethodMeta, requestMappingMate));
        }
        tags.put("supportBody", supportRequestBody);
        tags.put("responseType", this.getResponseType(requestMappingMate, javaMethodMeta, javaMethodMeta.getOwner()));
        List<String> needDeleteParams = new ArrayList<>();
        tags.put("url", getRequestUrl(javaMethodMeta, requestMappingMate, needDeleteParams).replace(prefix, ""));
        tags.put("hasPathVariable", !needDeleteParams.isEmpty());
        List<String> requestHeaderNames = getRequestHeaderNames(javaMethodMeta);
        tags.put("requestHeaderNames", requestHeaderNames);
        needDeleteParams.addAll(requestHeaderNames);
        tags.put("needDeleteParams", needDeleteParams);
    }

    private String getRequestUrl(JavaMethodMeta javaMethodMeta, RequestMappingMetaFactory.RequestMappingMate requestMappingMate, List<String> needDeleteParams) {
        String url = RequestMappingUtils.combinePath(requestMappingMate, javaMethodMeta.getMethod());
        Map<String, Annotation[]> paramAnnotations = javaMethodMeta.getParamAnnotations();
        for (Map.Entry<String, Annotation[]> entry : paramAnnotations.entrySet()) {
            String name = entry.getKey();
            Annotation[] annotations = entry.getValue();
            Optional<PathVariable> pathVariableOptional = RequestMappingUtils.findPathVariable(annotations);
            if (pathVariableOptional.isPresent()) {
                PathVariable pathVariable = pathVariableOptional.get();
                String pathVariableName = pathVariable.value();
                if (!StringUtils.hasText(pathVariableName)) {
                    pathVariableName = pathVariable.name();
                }
                if (!StringUtils.hasText(pathVariableName)) {
                    pathVariableName = name;
                }
                if (pathVariable.required()) {
                    url = url.replace("{" + pathVariableName + "}", "${" + pathVariableName + "}");
                } else {
                    //  处理PathVariable 非必填
                    String param = pathVariableName + "==null?" + "'':'/'+" + pathVariableName;
                    url = url.replace("/{" + pathVariableName + "}", "${" + param + "}");
                }
                needDeleteParams.add(pathVariableName);
            }
        }
        return url;
    }

    /**
     * 获取umi-request的请求类型
     *
     * @param javaMethodMeta     方法元数据
     * @param requestMappingMate RequestMappingMate
     * @return umi-request的请求类型
     */
    private String getRequestType(JavaMethodMeta javaMethodMeta, RequestMappingMetaFactory.RequestMappingMate requestMappingMate) {
        boolean hasRequestBody = hasRequestBody(javaMethodMeta);
        String[] consumes = requestMappingMate.consumes();
        boolean isEmpty = consumes.length == 0;
        if (hasRequestBody) {
            if (isEmpty) {
                consumes = new String[]{MediaType.APPLICATION_JSON_VALUE};
            }
        } else {
            if (isEmpty) {
                // 如果支持body 在没有{@link RequestBody}注解的情况下则使默认使用表单
                consumes = new String[]{MediaType.APPLICATION_FORM_URLENCODED_VALUE};
            }
        }
        return getContentTypeAlisName(consumes[0]);
    }

    /**
     * 获取请求头参数名称列表
     *
     * @param javaMethodMeta 方法元数据
     * @return 请求头参数名称列表
     */
    private List<String> getRequestHeaderNames(JavaMethodMeta javaMethodMeta) {
        List<String> requestHeaderNames = new ArrayList<>();
        Map<String, Annotation[]> paramAnnotations = javaMethodMeta.getParamAnnotations();
        for (Map.Entry<String, Annotation[]> entry : paramAnnotations.entrySet()) {
            Annotation[] annotations = entry.getValue();
            Optional<RequestHeader> optional = RequestMappingUtils.findRequestHeader(annotations);
            if (!optional.isPresent()) {
                continue;
            }
            RequestHeader requestHeader = optional.get();
            String name = requestHeader.value();
            if (!StringUtils.hasText(name)) {
                name = requestHeader.name();
            }
            if (!StringUtils.hasText(name)) {
                name = entry.getKey();
            }
            requestHeaderNames.add(name);

        }
        return requestHeaderNames;
    }

    private String getResponseType(RequestMappingMetaFactory.RequestMappingMate requestMappingMate, JavaMethodMeta javaMethodMeta,
                                   Class<?> methodOwner) {
        String[] produces = requestMappingMate.produces();
        boolean useResponseBody = javaMethodMeta.existAnnotation(ResponseBody.class) || methodOwner.isAnnotationPresent(RestController.class);
        boolean isEmpty = produces.length == 0;
        if (useResponseBody) {
            if (isEmpty) {
                produces = new String[]{MediaType.APPLICATION_JSON_VALUE};
            }
        } else {
            if (isEmpty) {
                boolean isNoneReturnFile = Arrays.stream(javaMethodMeta.getReturnType()).noneMatch(clazz -> JavaTypeUtils.isAssignableFrom(clazz, InputStreamResource.class));
                if (isNoneReturnFile) {
                    produces = new String[]{MediaType.TEXT_PLAIN_VALUE};
                } else {
                    produces = new String[]{MediaType.APPLICATION_OCTET_STREAM_VALUE};
                }
            }
        }
        String produce = produces[0];
        return getContentTypeAlisName(produce);
    }

    private String getContentTypeAlisName(String contentType) {
        switch (contentType) {
            case MediaType.APPLICATION_FORM_URLENCODED_VALUE:
            case MediaType.MULTIPART_FORM_DATA_VALUE:
                return "form";
            case MediaType.TEXT_HTML_VALUE:
            case MediaType.TEXT_PLAIN_VALUE:
                return "text";
            case MediaType.APPLICATION_OCTET_STREAM_VALUE:
                return "blob";
            case MediaType.APPLICATION_JSON_VALUE:
            case MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE:
                return "json";
            default:
                return null;
        }
    }

    private boolean hasRequestBody(JavaMethodMeta javaMethodMeta) {
        return javaMethodMeta.getParamAnnotations()
                .values()
                .stream()
                .anyMatch(annotations -> Arrays.stream(annotations)
                        .anyMatch(annotation -> RequestBody.class.isAssignableFrom(annotation.annotationType())));
    }

}
