package com.wuxp.codegen.languages.typescript;

import com.wuxp.codegen.annotation.processors.spring.RequestMappingProcessor;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;

/**
 * 基于umi request的增强配置
 *
 * @author wuxp
 */
public class UmiRequestEnhancedProcessor implements
    LanguageEnhancedProcessor<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {


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
      throw new RuntimeException("方法：" + javaMethodMeta.getName() + "没有RequestMapping相关注解");
    }
    RequestMappingProcessor.RequestMappingMate requestMappingMate = mappingAnnotation.get();
    RequestMethod requestMethod = requestMappingMate.getRequestMethod();
    tags.put("httpMethod", requestMethod.name().toLowerCase());
    boolean supportRequestBody = RequestMappingProcessor.isSupportRequestBody(requestMethod);
    tags.put("supportBody", supportRequestBody);
    if (supportRequestBody) {
      String[] consumes = requestMappingMate.consumes();
      if (consumes.length == 0) {
        // 如果支持body 则使默认使用表单
        consumes = new String[]{MediaType.APPLICATION_FORM_URLENCODED_VALUE};
      }
      String consume = consumes[0];
      tags.put("mediaType", consume);
      tags.put("useForm", MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(consume));
      tags.put("responseType", this.getResponseType(requestMappingMate, javaMethodMeta, classMeta));
    }
    tags.put("url", getRequestUrl(javaMethodMeta, requestMappingMate));
    return methodMeta;
  }

  private String getRequestUrl(JavaMethodMeta javaMethodMeta, RequestMappingProcessor.RequestMappingMate requestMappingMate) {
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
    return url;
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
    switch (produce) {
      case MediaType.APPLICATION_FORM_URLENCODED_VALUE:
        return "form";
      case MediaType.APPLICATION_JSON_VALUE:
      case MediaType.APPLICATION_PROBLEM_JSON_UTF8_VALUE:
        return "json";
      case MediaType.TEXT_HTML_VALUE:
      case MediaType.TEXT_PLAIN_VALUE:
        return "text";
      case MediaType.APPLICATION_OCTET_STREAM_VALUE:
        return "blob";
      default:
        return null;
    }
  }


}
