package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.annotation.processors.spring.RequestMappingProcessor;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.util.RequestMappingUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 基于umi request的增强配置
 *
 * @author wuxp
 * <a href="https://github.com/umijs/umi-request"></a>
 */
public class UmiRequestEnhancedProcessor implements
        LanguageEnhancedProcessor<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {

    /**
     * umi-request的prefix 配置，在生成是将从url中移除该部分内容
     */
    private final String prefix;

    public UmiRequestEnhancedProcessor() {
        this("");
    }

    public UmiRequestEnhancedProcessor(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public CommonCodeGenMethodMeta enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta,
                                                            JavaClassMeta classMeta) {
        // delete 是typescript的关键字，不能用作方法名称
        if ("delete".equals(methodMeta.getName())) {
            methodMeta.setName("deleteRequest");
        }

        Map<String, Object> tags = methodMeta.getTags();
        Optional<RequestMappingProcessor.RequestMappingMate> mappingAnnotation = RequestMappingUtils
                .findRequestMappingAnnotation(javaMethodMeta.getAnnotations());
        if (!mappingAnnotation.isPresent()) {
            throw new CodegenRuntimeException("方法：" + javaMethodMeta.getName() + "没有RequestMapping相关注解");
        }
        RequestMappingProcessor.RequestMappingMate requestMappingMate = mappingAnnotation.get();
        RequestMethod requestMethod = requestMappingMate.getRequestMethod();
        tags.put("httpMethod", requestMethod.name().toLowerCase());
        boolean supportRequestBody = RequestMappingProcessor.isSupportRequestBody(requestMethod);
        if (supportRequestBody) {
            tags.put("requestType", this.getRequestType(javaMethodMeta, requestMappingMate));
        }
        tags.put("supportBody", supportRequestBody);
        tags.put("responseType", this.getResponseType(requestMappingMate, javaMethodMeta, classMeta));
        List<String> needDeleteParams = new ArrayList<>();
        tags.put("url", getRequestUrl(javaMethodMeta, requestMappingMate, needDeleteParams).replace(prefix, ""));
        tags.put("hasPathVariable", !needDeleteParams.isEmpty());
        List<String> requestHeaderNames = getRequestHeaderNames(javaMethodMeta);
        tags.put("requestHeaderNames", requestHeaderNames);
        needDeleteParams.addAll(requestHeaderNames);
        tags.put("needDeleteParams", needDeleteParams);
        return methodMeta;
    }

    private String getRequestUrl(JavaMethodMeta javaMethodMeta, RequestMappingProcessor.RequestMappingMate requestMappingMate, List<String> needDeleteParams) {
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
                    url = url.replace("{" + pathVariableName + "}", "${req." + pathVariableName + "}");
                } else {
                    //  处理PathVariable 非必填
                    String param = "req." + pathVariableName;
                    param = param + "==null?" + "'':'/'+" + param;
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
    private String getRequestType(JavaMethodMeta javaMethodMeta, RequestMappingProcessor.RequestMappingMate requestMappingMate) {
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

    private String getResponseType(RequestMappingProcessor.RequestMappingMate requestMappingMate, JavaMethodMeta javaMethodMeta,
                                   JavaClassMeta classMeta) {
        String[] produces = requestMappingMate.produces();
        boolean useResponseBody = javaMethodMeta.existAnnotation(ResponseBody.class) || classMeta.existAnnotation(RestController.class);
        if (useResponseBody) {
            if (produces.length == 0) {
                produces = new String[]{MediaType.APPLICATION_JSON_VALUE};
            }
        } else {
            if (produces.length == 0) {
                produces = new String[]{MediaType.TEXT_PLAIN_VALUE};
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
