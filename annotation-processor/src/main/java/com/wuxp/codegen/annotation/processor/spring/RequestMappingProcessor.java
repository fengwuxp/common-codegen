package com.wuxp.codegen.annotation.processor.spring;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.transform.AnnotationCodeGenTransformer;
import com.wuxp.codegen.transform.spring.JavaRetofitRequestMappingTransformer;
import com.wuxp.codegen.transform.spring.TypeScriptRequestMappingTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;


/**
 * <p>
 *
 * @see RequestMapping
 * 处理spring mvc的RequestMapping相关注解
 *
 * <p>
 */
@Slf4j
public class RequestMappingProcessor extends AbstractAnnotationProcessor<Annotation, RequestMappingProcessor.RequestMappingMate> {


    //Mapping和mapping元数据的对应
    private static final Map<Class<? extends Annotation>, Class<? extends RequestMappingProcessor.RequestMappingMate>> ANNOTATION_CLASS_MAP = new LinkedHashMap<>();


    //注解转换器和语言类型的对应关系
    private static final Map<LanguageDescription, AnnotationCodeGenTransformer<CommonCodeGenAnnotation, RequestMappingMate>> ANNOTATION_CODE_GEN_TRANSFORMER_MAP = new HashMap<>();

    static {
        ANNOTATION_CLASS_MAP.put(RequestMapping.class, RequestMappingProcessor.RequestMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PostMapping.class, RequestMappingProcessor.PostMappingMate.class);
        ANNOTATION_CLASS_MAP.put(GetMapping.class, RequestMappingProcessor.GetMappingMate.class);
        ANNOTATION_CLASS_MAP.put(DeleteMapping.class, RequestMappingProcessor.DeleteMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PutMapping.class, RequestMappingProcessor.PutMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PatchMapping.class, RequestMappingProcessor.PatchMappingMate.class);


        ANNOTATION_CODE_GEN_TRANSFORMER_MAP.put(LanguageDescription.JAVA_ANDROID, new JavaRetofitRequestMappingTransformer());
        ANNOTATION_CODE_GEN_TRANSFORMER_MAP.put(LanguageDescription.TYPESCRIPT, new TypeScriptRequestMappingTransformer());
    }


    @Override
    public RequestMappingMate process(Annotation annotation) {

        Class<? extends RequestMappingMate> clazz = ANNOTATION_CLASS_MAP.get(annotation.annotationType());
        if (clazz == null) {
            throw new RuntimeException("not spring mapping annotation，annotation name: " + annotation.annotationType().getName());
        }

        return this.newProxyMate(annotation, clazz);

    }


    public abstract static class RequestMappingMate implements AnnotationMate<Annotation>, RequestMapping {

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
            codeGenAnnotation.setName("Feign");
            codeGenAnnotation.getPositionArguments().remove("method");
            return codeGenAnnotation;
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Method annotationOwner) {

            return this.genAnnotation(annotationOwner);
        }


        /**
         * 获取请求方法
         *
         * @return
         */
        public RequestMethod getRequestMethod() {

            RequestMethod[] requestMethods = this.method();

            if (requestMethods.length == 0) {
                return RequestMethod.POST;
            } else {
                return requestMethods[0];
            }
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
