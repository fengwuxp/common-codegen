package com.wuxp.codegen.languages.java;

import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

import static com.wuxp.codegen.meta.transform.spring.SpringRequestMappingTransformer.SPRING_OPENFEIGN_CLIENT_ANNOTATION_NAME;

/**
 * 增强 spring feign client 生成时的相关的注解
 *
 * @author wuxp
 * @see org.springframework.cloud.openfeign.FeignClient
 */
public class SpringCloudFeignClientPostProcessor implements LanguageDefinitionPostProcessor<CommonBaseMeta> {

    private static final CommonCodeGenAnnotation SPRING_CLOUD_FEIGN_QUERY_MAP;

    static {
        SPRING_CLOUD_FEIGN_QUERY_MAP = new CommonCodeGenAnnotation();
        SPRING_CLOUD_FEIGN_QUERY_MAP.setName("SpringQueryMap");
        Map<String, String> namedArguments = new HashMap<>();
        namedArguments.put("value", "true");
        SPRING_CLOUD_FEIGN_QUERY_MAP.setNamedArguments(namedArguments);
    }

    private final Map<String, String> feignAttrs;

    private SpringCloudFeignClientPostProcessor(Map<String, String> feignAttrs) {
        this.feignAttrs = feignAttrs;
    }

    public static SpringCloudFeignClientPostProcessorBuilder builder() {
        return new SpringCloudFeignClientPostProcessorBuilder();
    }

    @Override
    public void postProcess(CommonBaseMeta meta) {
        if (meta instanceof CommonCodeGenAnnotation) {
            fillAnnotationAttributes((CommonCodeGenAnnotation) meta);
        }
        if (meta instanceof CommonCodeGenMethodMeta) {
            addSpringQueryMapAnnotation((CommonCodeGenMethodMeta) meta);
        }
    }


    public CommonCodeGenAnnotation fillAnnotationAttributes(CommonCodeGenAnnotation annotation) {
        if (annotation.getAnnotationOwner() instanceof Class) {
            if (!SPRING_OPENFEIGN_CLIENT_ANNOTATION_NAME.equals(annotation.getName())) {
                return annotation;
            }
            Map<String, String> namedArguments = annotation.getNamedArguments();
            namedArguments.putAll(feignAttrs);
            annotation.setNamedArguments(new TreeMap<>(namedArguments));
        }

        return annotation;
    }

    public void addSpringQueryMapAnnotation(CommonCodeGenMethodMeta methodMeta) {
        JavaMethodMeta javaMethodMeta = methodMeta.getJavaMethodMeta();
        Optional<RequestMappingMetaFactory.RequestMappingMate> requestMappingAnnotation = RequestMappingUtils
                .findRequestMappingAnnotation(javaMethodMeta.getAnnotations());
        if (!requestMappingAnnotation.isPresent()) {
            throw new CodegenRuntimeException("方法：" + javaMethodMeta.getName() + "没有RequestMapping相关注解");
        }
        RequestMethod requestMethod = requestMappingAnnotation.get().getRequestMethod();
        if (!RequestMethod.GET.equals(requestMethod)) {
            return;
        }
        javaMethodMeta.getParameters().forEach((name, parameter) -> {
            if (JavaTypeUtils.isNoneJdkComplex(parameter.getType())) {
                // 对于 GET 请求的复杂参数 增加 SpringQueryMap 注解
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
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenAnnotation.class) ||
                JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenMethodMeta.class);
    }

    public static class SpringCloudFeignClientPostProcessorBuilder {
        private static final String FORMAT_PATTERN = "\"%s\"";

        private final Map<String, String> feignAttrs = new HashMap<>(8);

        public SpringCloudFeignClientPostProcessorBuilder name(String name) {
            feignAttrs.put("name", String.format(FORMAT_PATTERN, name));
            return this;
        }

        public SpringCloudFeignClientPostProcessorBuilder url(String url) {

            feignAttrs.put("url", String.format(FORMAT_PATTERN, url));
            return this;
        }

        public SpringCloudFeignClientPostProcessorBuilder contextId(String contextId) {
            feignAttrs.put("contextId", String.format(FORMAT_PATTERN, contextId));
            return this;
        }

        public SpringCloudFeignClientPostProcessorBuilder decode404(boolean decode404) {
            feignAttrs.put("decode404", Boolean.toString(decode404));
            return this;
        }

        public SpringCloudFeignClientPostProcessor build() {
            return new SpringCloudFeignClientPostProcessor(this.feignAttrs);
        }
    }
}
