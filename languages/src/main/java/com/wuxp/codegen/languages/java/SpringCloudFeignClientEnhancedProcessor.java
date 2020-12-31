package com.wuxp.codegen.languages.java;

import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import com.wuxp.codegen.util.RequestMappingUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.*;

import static com.wuxp.codegen.transform.spring.SpringRequestMappingTransformer.SPRING_OPENFEIGN_CLIENT_ANNOTATION_NAME;

/**
 * 增强spring feign client 生成时的相关的注解
 *
 * @author wuxp
 * @see org.springframework.cloud.openfeign.FeignClient
 */
public class SpringCloudFeignClientEnhancedProcessor implements LanguageEnhancedProcessor<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {

    private final static CommonCodeGenAnnotation SPRING_CLOUD_FEIGN_QUERY_MAP;

    static {
        SPRING_CLOUD_FEIGN_QUERY_MAP = new CommonCodeGenAnnotation();
        SPRING_CLOUD_FEIGN_QUERY_MAP.setName("SpringQueryMap");
        Map<String, String> namedArguments = new HashMap<>();
        namedArguments.put("value", "true");
        SPRING_CLOUD_FEIGN_QUERY_MAP.setNamedArguments(namedArguments);
    }

    private final Map<String, String> feignAttrs;

    private SpringCloudFeignClientEnhancedProcessor(Map<String, String> feignAttrs) {
        this.feignAttrs = feignAttrs;
    }

    public static SpringCloudFeignClientEnhancedProcessorBuilder builder() {
        return new SpringCloudFeignClientEnhancedProcessorBuilder();
    }


    @Override
    public CommonCodeGenAnnotation enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, Annotation annotation, Object annotationOwner) {
        if (annotationOwner instanceof Class) {
            if (!SPRING_OPENFEIGN_CLIENT_ANNOTATION_NAME.equals(codeGenAnnotation.getName())) {
                return codeGenAnnotation;
            }
            Map<String, String> namedArguments = codeGenAnnotation.getNamedArguments();
            namedArguments.putAll(feignAttrs);
            codeGenAnnotation.setNamedArguments(new TreeMap<>(namedArguments));
        }

        return codeGenAnnotation;
    }

    @Override
    public CommonCodeGenMethodMeta enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

        Optional<RequestMappingProcessor.RequestMappingMate> requestMappingAnnotation = RequestMappingUtils.findRequestMappingAnnotation(javaMethodMeta.getAnnotations());
        if (!requestMappingAnnotation.isPresent()) {
            throw new RuntimeException("方法" + javaMethodMeta.getName() + "没有RequestMapping相关组件");
        }
        RequestMethod requestMethod = requestMappingAnnotation.get().getRequestMethod();
        if (!RequestMethod.GET.equals(requestMethod)) {
            return methodMeta;
        }
        Map<String, Parameter> parameters = javaMethodMeta.getParameters();
        parameters.forEach((name, parameter) -> {
            if (JavaTypeUtil.isNoneJdkComplex(parameter.getType())) {
                // 对于GET请求的复杂参数 增加SpringQueryMap注解
                CommonCodeGenAnnotation[] annotations = methodMeta.getParamAnnotations().get(name);
                if (annotations == null) {
                    annotations = new CommonCodeGenAnnotation[]{SPRING_CLOUD_FEIGN_QUERY_MAP};
                } else {
                    List<CommonCodeGenAnnotation> commonCodeGenAnnotations = new ArrayList<>(annotations.length + 1);
                    commonCodeGenAnnotations.add(SPRING_CLOUD_FEIGN_QUERY_MAP);
                    commonCodeGenAnnotations.addAll(Arrays.asList(annotations));
                    annotations = commonCodeGenAnnotations.toArray(new CommonCodeGenAnnotation[0]);
                }
                methodMeta.getParamAnnotations().put(name, annotations);
            }
        });
        return methodMeta;
    }

    @Override
    public void setCodeGenMatchers(List<CodeGenMatcher> codeGenMatchers) {

    }

    public static class SpringCloudFeignClientEnhancedProcessorBuilder {

        private final Map<String, String> feignAttrs = new HashMap<>(8);

        public SpringCloudFeignClientEnhancedProcessorBuilder name(String name) {
            feignAttrs.put("name", String.format("\"%s\"", name));
            return this;
        }

        public SpringCloudFeignClientEnhancedProcessorBuilder url(String url) {
            feignAttrs.put("url", String.format("\"%s\"", url));
            return this;
        }

        public SpringCloudFeignClientEnhancedProcessorBuilder contextId(String contextId) {
            feignAttrs.put("contextId", String.format("\"%s\"", contextId));
            return this;
        }

        public SpringCloudFeignClientEnhancedProcessorBuilder decode404(boolean decode404) {
            feignAttrs.put("decode404", decode404 + "");
            return this;
        }

        public SpringCloudFeignClientEnhancedProcessor build() {
            return new SpringCloudFeignClientEnhancedProcessor(this.feignAttrs);
        }
    }
}
