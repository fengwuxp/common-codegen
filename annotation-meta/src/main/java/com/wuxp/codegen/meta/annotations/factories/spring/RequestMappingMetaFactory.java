package com.wuxp.codegen.meta.annotations.factories.spring;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import com.wuxp.codegen.meta.enums.AuthenticationType;
import com.wuxp.codegen.meta.transform.retrofit.RetrofitRequestMappingTransformer;
import com.wuxp.codegen.meta.transform.spring.DartRequestMappingTransformer;
import com.wuxp.codegen.meta.transform.spring.SpringRequestMappingTransformer;
import com.wuxp.codegen.meta.transform.spring.TypeScriptRequestMappingTransformer;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.constant.MappingAnnotationPropNameConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wxup
 * @see RequestMapping
 * 处理spring mvc的RequestMapping相关注解
 */
@Slf4j
public class RequestMappingMetaFactory extends AbstractAnnotationMetaFactory<Annotation, RequestMappingMetaFactory.RequestMappingMate> {


    /**
     * Mapping和mapping元数据的对应
     */
    private static final Map<Class<? extends Annotation>, Class<? extends RequestMappingMetaFactory.RequestMappingMate>> ANNOTATION_CLASS_MAP = new LinkedHashMap<>();

    /**
     * 需要认证的类型和相关的路径列表，使用ant匹配
     */
    private static final Map<AuthenticationType, String[]> AUTHENTICATION_TYPE_PATHS = new LinkedHashMap<>();

    /**
     * 是否支持{@link MappingAnnotationPropNameConstant#AUTHENTICATION_TYPE}
     */
    private static boolean supportAuthenticationType = false;

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final List<RequestMethod> SUPPORT_BODY_METHODS = Arrays.asList(RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH);

    static {
        ANNOTATION_CLASS_MAP.put(RequestMapping.class, RequestMappingMetaFactory.RequestMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PostMapping.class, RequestMappingMetaFactory.PostMappingMate.class);
        ANNOTATION_CLASS_MAP.put(GetMapping.class, RequestMappingMetaFactory.GetMappingMate.class);
        ANNOTATION_CLASS_MAP.put(DeleteMapping.class, RequestMappingMetaFactory.DeleteMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PutMapping.class, RequestMappingMetaFactory.PutMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PatchMapping.class, RequestMappingMetaFactory.PatchMappingMate.class);


        registerAnnotationTransformer(ClientProviderType.SPRING_CLOUD_OPENFEIGN, RequestMapping.class, new SpringRequestMappingTransformer());
        registerAnnotationTransformer(ClientProviderType.RETROFIT, RequestMapping.class, new RetrofitRequestMappingTransformer());
        registerAnnotationTransformer(ClientProviderType.TYPESCRIPT_FEIGN, RequestMapping.class, new TypeScriptRequestMappingTransformer());
        registerAnnotationTransformer(ClientProviderType.DART_FEIGN, RequestMapping.class, new DartRequestMappingTransformer());
        registerAnnotationTransformer(ClientProviderType.UMI_REQUEST, RequestMapping.class, new TypeScriptRequestMappingTransformer());
        registerAnnotationTransformer(ClientProviderType.AXIOS, RequestMapping.class, new TypeScriptRequestMappingTransformer());
    }

    /**
     * 是否支持 annotation
     *
     * @param annotation 注解
     */
    public static boolean isSupportAnnotationType(Annotation annotation) {
        if (annotation == null) {
            return false;
        }
        return ANNOTATION_CLASS_MAP.containsKey(annotation.annotationType());
    }

    /**
     * http method 方法是否支持 Request body
     *
     * @param method http 请求方法
     */
    public static boolean isSupportRequestBody(RequestMethod method) {
        return SUPPORT_BODY_METHODS.contains(method);
    }


    @Override
    public RequestMappingMate factory(Annotation annotation) {
        Class<? extends RequestMappingMate> clazz = ANNOTATION_CLASS_MAP.get(annotation.annotationType());
        if (clazz == null) {
            throw new CodegenRuntimeException(MessageFormat.format("not spring mapping annotation，annotation name: {0}", annotation.annotationType().getName()));
        }
        return this.newProxyMate(annotation, clazz);

    }

    public static void addAuthenticationTypePaths(AuthenticationType type, String[] paths) {
        AUTHENTICATION_TYPE_PATHS.put(type, paths);
    }

    public static void setSupportAuthenticationType(boolean supportAuthenticationType) {
        RequestMappingMetaFactory.supportAuthenticationType = supportAuthenticationType;
    }

    public abstract static class RequestMappingMate implements AnnotationMate, RequestMapping {

        public String[] getPath() {
            String[] value = this.value();
            if (value.length > 0) {
                return value;
            }
            value = this.path();
            if (value.length > 0) {
                return value;
            }
            return new String[]{};
        }

        @Override
        public String toComment(Class<?> annotationOwner) {
            return MessageFormat.format("接口：{0}", this.getRequestMethod().name());
        }


        @Override
        public String toComment(Method annotationOwner) {
            return MessageFormat.format("Http请求方法：{0}", this.getRequestMethod().name());
        }


        @Override
        public CommonCodeGenAnnotation toAnnotation(Class<?> annotationOwner) {
            CommonCodeGenAnnotation annotation = this.genAnnotation(annotationOwner);
            if (annotation == null) {
                return null;
            }
            annotation.getPositionArguments().remove("method");
            annotation.setElementType(ElementType.TYPE);
            return annotation;
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Method annotationOwner) {
            CommonCodeGenAnnotation annotation = this.genAnnotation(annotationOwner);
            if (!supportAuthenticationType) {
                return annotation;
            }
            trySetAuthenticationType(annotationOwner, annotation);
            annotation.setElementType(ElementType.METHOD);
            return annotation;
        }

        private void trySetAuthenticationType(Method annotationOwner, CommonCodeGenAnnotation codeGenAnnotation) {
            if (AUTHENTICATION_TYPE_PATHS.isEmpty()) {
                return;
            }
            assert codeGenAnnotation != null;
            Map<String, String> namedArguments = codeGenAnnotation.getNamedArguments();
            String requestUri = RequestMappingUtils.combinePath(this, annotationOwner);

            AUTHENTICATION_TYPE_PATHS.forEach((key, value) -> Arrays.stream(value)
                    .filter(pattern -> PATH_MATCHER.match(pattern, requestUri))
                    .findFirst()
                    .ifPresent(matchResult -> {
                        Assert.isTrue(!namedArguments.containsKey(MappingAnnotationPropNameConstant.AUTHENTICATION_TYPE),
                                "路径匹配到多种认证类型,path = " + requestUri);
                        String type = String.format("AuthenticationType.%s", key.name());
                        namedArguments.put(MappingAnnotationPropNameConstant.AUTHENTICATION_TYPE, type);
                        codeGenAnnotation.getPositionArguments().add(type);
                    }));
        }


        /**
         * 获取请求方法
         *
         * @return http 请求方法
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
         * 是否为 GET 请求
         */
        public boolean isGetMethod() {
            return RequestMethod.GET.equals(getRequestMethod());
        }


        /**
         * 生成 RequestMapping 相关注解
         *
         * @param annotationOwner 所有者的name 如果ownerName和value中的一致，则不生成value
         * @return 用于生成的注解
         */
        private CommonCodeGenAnnotation genAnnotation(Object annotationOwner) {
            return getAnnotationTransformer(RequestMapping.class).transform(this, annotationOwner);
        }
    }

    public abstract static class GetMappingMate extends RequestMappingMate {

        private final RequestMethod[] methods = new RequestMethod[]{RequestMethod.GET};

        @Override
        @NonNull
        public RequestMethod[] method() {
            return methods;
        }

    }

    public abstract static class PostMappingMate extends RequestMappingMate {

        private final RequestMethod[] methods = new RequestMethod[]{RequestMethod.POST};

        @Override
        @NonNull
        public RequestMethod[] method() {
            return methods;
        }
    }

    public abstract static class DeleteMappingMate extends RequestMappingMate {

        private final RequestMethod[] methods = new RequestMethod[]{RequestMethod.DELETE};

        @Override
        @NonNull
        public RequestMethod[] method() {
            return methods;
        }

    }

    public abstract static class PutMappingMate extends RequestMappingMate {

        private final RequestMethod[] methods = new RequestMethod[]{RequestMethod.PUT};

        @Override
        @NonNull
        public RequestMethod[] method() {
            return methods;
        }

    }

    public abstract static class PatchMappingMate extends RequestMappingMate {

        private final RequestMethod[] methods = new RequestMethod[]{RequestMethod.PATCH};

        @Override
        @NonNull
        public RequestMethod[] method() {
            return methods;
        }

    }

}
