package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.wuxp.codegen.transform.spring.SpringRequestMappingTransformer.SPRING_OPENFEIGN_CLIENT_ANNOTATION_NAME;

/**
 * 增强spring feign client 生成时的相关的注解
 *
 * @author wuxp
 * @see org.springframework.cloud.openfeign.FeignClient
 */
public class SpringCloudFeignClientEnhancedProcessor implements LanguageEnhancedProcessor<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {

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
