package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.util.RequestMappingUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;

/**
 * 基于umi request的增强配置
 *
 * @author wuxp
 */
public class UmiRequestEnhancedProcessor implements LanguageEnhancedProcessor<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {


    @Override
    public CommonCodeGenMethodMeta enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {
        Map<String, Object> tags = methodMeta.getTags();
        Optional<RequestMappingProcessor.RequestMappingMate> mappingAnnotation = RequestMappingUtils.findRequestMappingAnnotation(javaMethodMeta.getAnnotations());
        if (!mappingAnnotation.isPresent()) {
            throw new RuntimeException("方法：" + javaMethodMeta.getName() + "没有RequestMapping相关注解");
        }
        RequestMappingProcessor.RequestMappingMate requestMappingMate = mappingAnnotation.get();
        RequestMethod requestMethod = requestMappingMate.getRequestMethod();
        tags.put("httpMethod", requestMethod.name().toLowerCase());
        boolean supportRequestBody = RequestMappingProcessor.isSupportRequestBody(requestMethod);
        tags.put("supportBody", supportRequestBody);
        String[] consumes = requestMappingMate.consumes();
        if (consumes.length == 0) {
            // 如果支持body 则使默认使用表单
            consumes = new String[]{MediaType.APPLICATION_FORM_URLENCODED_VALUE};
        }
        if (supportRequestBody) {
            tags.put("mediaType", consumes[0]);
        }
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
            }
        }

        tags.put("url", url);
        return methodMeta;
    }
}
