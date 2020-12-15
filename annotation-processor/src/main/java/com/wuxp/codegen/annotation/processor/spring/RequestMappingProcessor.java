package com.wuxp.codegen.annotation.processor.spring;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.enums.AuthenticationType;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.constant.MappingAnnotationPropNameConstant;
import com.wuxp.codegen.transform.AnnotationCodeGenTransformer;
import com.wuxp.codegen.transform.spring.DartRequestMappingTransformer;
import com.wuxp.codegen.transform.spring.JavaRetofitRequestMappingTransformer;
import com.wuxp.codegen.transform.spring.TypeScriptRequestMappingTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;


/**
 * @author wxup
 * @see RequestMapping
 * 处理spring mvc的RequestMapping相关注解
 */
@Slf4j
public class RequestMappingProcessor extends AbstractAnnotationProcessor<Annotation, RequestMappingProcessor.RequestMappingMate> {


    public static final String FEIGN_CLIENT_ANNOTATION_NAME = "Feign";

    //Mapping和mapping元数据的对应
    private static final Map<Class<? extends Annotation>, Class<? extends RequestMappingProcessor.RequestMappingMate>> ANNOTATION_CLASS_MAP = new LinkedHashMap<>();


    //注解转换器和语言类型的对应关系
    private static final Map<LanguageDescription, AnnotationCodeGenTransformer<CommonCodeGenAnnotation, RequestMappingMate>> ANNOTATION_CODE_GEN_TRANSFORMER_MAP = new HashMap<>();

    /**
     * 需要认证的类型和相关的路径列表，使用ant匹配
     */
    private static final Map<AuthenticationType, String[]> AUTHENTICATION_PATH = new LinkedHashMap<>();

    /**
     * 是否支持{@link MappingAnnotationPropNameConstant#AUTHENTICATION_TYPE}
     */
    private static boolean supportAuthenticationType = false;

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    static {
        ANNOTATION_CLASS_MAP.put(RequestMapping.class, RequestMappingProcessor.RequestMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PostMapping.class, RequestMappingProcessor.PostMappingMate.class);
        ANNOTATION_CLASS_MAP.put(GetMapping.class, RequestMappingProcessor.GetMappingMate.class);
        ANNOTATION_CLASS_MAP.put(DeleteMapping.class, RequestMappingProcessor.DeleteMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PutMapping.class, RequestMappingProcessor.PutMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PatchMapping.class, RequestMappingProcessor.PatchMappingMate.class);


        ANNOTATION_CODE_GEN_TRANSFORMER_MAP.put(LanguageDescription.JAVA_ANDROID, new JavaRetofitRequestMappingTransformer());
        ANNOTATION_CODE_GEN_TRANSFORMER_MAP.put(LanguageDescription.TYPESCRIPT, new TypeScriptRequestMappingTransformer());
        ANNOTATION_CODE_GEN_TRANSFORMER_MAP.put(LanguageDescription.DART, new DartRequestMappingTransformer());
    }


    @Override
    public RequestMappingMate process(Annotation annotation) {

        Class<? extends RequestMappingMate> clazz = ANNOTATION_CLASS_MAP.get(annotation.annotationType());
        if (clazz == null) {
            throw new RuntimeException(MessageFormat.format("not spring mapping annotation，annotation name: {0}", annotation.annotationType().getName()));
        }

        return this.newProxyMate(annotation, clazz);

    }

    /**
     * 设置 注解处理器
     *
     * @param languageDescription
     * @param transformer
     */
    public static void setAnnotationCodeGenTransformer(LanguageDescription languageDescription, AnnotationCodeGenTransformer transformer) {
        ANNOTATION_CODE_GEN_TRANSFORMER_MAP.put(languageDescription, transformer);
    }

    public static void addAuthenticationTypePaths(AuthenticationType type, String[] paths) {
        AUTHENTICATION_PATH.put(type, paths);
    }

    public static void setSupportAuthenticationType(boolean supportAuthenticationType) {
        RequestMappingProcessor.supportAuthenticationType = supportAuthenticationType;
    }


    public abstract static class RequestMappingMate implements AnnotationMate<Annotation>, RequestMapping {

        private static final List<RequestMethod> SUPPORT_BODY_METHODS = Arrays.asList(RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH);

        @Override
        public String toComment(Class<?> annotationOwner) {

            return MessageFormat.format("接口：{0}", this.getRequestMethod().name());
        }


        @Override
        public String toComment(Method annotationOwner) {
            return MessageFormat.format("接口方法：{0}", this.getRequestMethod().name());
        }


        @Override
        public CommonCodeGenAnnotation toAnnotation(Class<?> annotationOwner) {

            CommonCodeGenAnnotation codeGenAnnotation = this.genAnnotation(annotationOwner);
            if (codeGenAnnotation == null) {
                return null;
            }
            //将类上的注解改为feign
            codeGenAnnotation.setName(FEIGN_CLIENT_ANNOTATION_NAME);
            codeGenAnnotation.getPositionArguments().remove("method");
            return codeGenAnnotation;
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Method annotationOwner) {
            CommonCodeGenAnnotation codeGenAnnotation = this.genAnnotation(annotationOwner);
            if (!supportAuthenticationType) {
                return codeGenAnnotation;
            }
            if (AUTHENTICATION_PATH.isEmpty()) {
                return codeGenAnnotation;
            }
            assert codeGenAnnotation != null;
            Map<String, String> namedArguments = codeGenAnnotation.getNamedArguments();
            String requestUri = this.getRequestUri(annotationOwner);

            AUTHENTICATION_PATH.forEach((key, value) -> {
                Arrays.stream(value)
                        .filter(pattern -> PATH_MATCHER.match(pattern, requestUri))
                        .findFirst()
                        .ifPresent(matchResult -> {
                            String type = "AuthenticationType." + key.name();
                            if (!namedArguments.containsKey(MappingAnnotationPropNameConstant.AUTHENTICATION_TYPE)) {
                                namedArguments.put(MappingAnnotationPropNameConstant.AUTHENTICATION_TYPE, type);
                                codeGenAnnotation.getPositionArguments().add(type);
                            } else {
                                // TODO
                                log.error("路径匹配到多种认证类型,path = {}", requestUri);
                            }
                        });
            });

            return codeGenAnnotation;
        }


        /**
         * 获取请求方法
         *
         * @return
         */
        public RequestMethod getRequestMethod() {

            RequestMethod[] requestMethods = this.method();

            if (requestMethods.length == 0) {
                return RequestMethod.GET;
            } else {
                return requestMethods[0];
            }
        }

        /**
         * http method 方法是否支持 Request body
         *
         * @param method
         */
        public static boolean isSupportRequestBody(RequestMethod method) {

            return SUPPORT_BODY_METHODS.contains(method);
        }

        /**
         * 获取请求 path
         *
         * @param annotationOwner
         * @return
         */
        private String getRequestUri(Method annotationOwner) {

            RequestMapping clazzMapping = annotationOwner.getDeclaringClass().getAnnotation(RequestMapping.class);
            String[] clazzMappingValues = clazzMapping == null ? new String[]{} : clazzMapping.value();
            String p1 = clazzMappingValues.length == 0 ? "" : clazzMappingValues[0].startsWith("/") ? clazzMappingValues[0] : "/" + clazzMappingValues[0];

            String[] methodsMappingValues = this.value();
            String p2 = methodsMappingValues.length == 0 ? "" : methodsMappingValues[0].startsWith("/") ? methodsMappingValues[0] : "/" + methodsMappingValues[0];

            return p1 + p2;
        }

        /**
         * 生成 RequestMapping 相关注解
         *
         * @param annotationOwner 所有者的name 如果ownerName和value中的一致，则不生成value
         * @return
         */
        private CommonCodeGenAnnotation genAnnotation(Object annotationOwner) {

            LanguageDescription languageDescription = CodegenBuilder.CODEGEN_GLOBAL_CONFIG.getLanguageDescription();
            if (languageDescription == null) {
                log.error("current languageDescription is null");
                return null;
            }
            AnnotationCodeGenTransformer<CommonCodeGenAnnotation, RequestMappingMate> transformer = ANNOTATION_CODE_GEN_TRANSFORMER_MAP.get(languageDescription);

            if (transformer == null) {
                log.error("not find {} transformer", languageDescription.getName());
                return null;
            }
            return transformer.transform(this, annotationOwner);

        }
    }

    public abstract static class GetMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.GET};

        @Override
        public RequestMethod[] method() {
            return methods;
        }

    }

    public abstract static class PostMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.POST};

        @Override
        public RequestMethod[] method() {
            return methods;
        }
    }

    public abstract static class DeleteMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.DELETE};

        @Override
        public RequestMethod[] method() {
            return methods;
        }

    }

    public abstract static class PutMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.PUT};

        @Override
        public RequestMethod[] method() {
            return methods;
        }

    }

    public abstract static class PatchMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.PATCH};

        @Override
        public RequestMethod[] method() {
            return methods;
        }

    }

}
