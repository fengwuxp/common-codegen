package com.wuxp.codegen.annotation.processor.spring;

import com.alibaba.fastjson.JSON;
import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *
 * @see RequestMapping
 * 处理spring mvc的RequestMapping相关注解
 *
 * <p>
 */
public class RequestMappingProcessor extends AbstractAnnotationProcessor<Annotation, RequestMappingProcessor.RequestMappingMate> {


    private static final Map<Class<? extends Annotation>, Class<? extends RequestMappingMate>> ANNOTATION_CLASS_MAP = new LinkedHashMap<>();


    //媒体类型映射
    private static final Map<String, String> MEDIA_TYPE_MAPPING = new LinkedHashMap<>();

    static {
        ANNOTATION_CLASS_MAP.put(RequestMapping.class, RequestMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PostMapping.class, PostMappingMate.class);
        ANNOTATION_CLASS_MAP.put(GetMapping.class, GetMappingMate.class);
        ANNOTATION_CLASS_MAP.put(DeleteMapping.class, DeleteMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PutMapping.class, PutMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PatchMapping.class, PatchMappingMate.class);

        MEDIA_TYPE_MAPPING.put(MediaType.MULTIPART_FORM_DATA_VALUE, "MediaType.FORM_DATA");
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_JSON_VALUE, "MediaType.JSON");
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

            return "接口的请求方法为：" + this.getRequestMethod().name();
        }


        @Override
        public String toComment(Method annotationOwner) {
            return "接口的请求方法为：" + this.getRequestMethod().name();
        }


        @Override
        public CommonCodeGenAnnotation toAnnotation(Class<?> annotationOwner) {

            return this.genAnnotation(annotationOwner.getSimpleName());
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Method annotationOwner) {

            return this.genAnnotation(annotationOwner.getName());
        }

        /**
         * 生成 RequestMapping 相关注解
         *
         * @param ownerName 所有者的name 如果ownerName和value中的一致，则不生成value
         * @return
         */
        protected CommonCodeGenAnnotation genAnnotation(String ownerName) {
            CommonCodeGenAnnotation codeGenAnnotation = new CommonCodeGenAnnotation();
            codeGenAnnotation.setName(this.annotationType().getSimpleName());

            //注解命名参数
            Map<String, String> arguments = new LinkedHashMap<>();
            String[] value = this.value();
            String val = null;
            if (value.length > 0) {
                val = value[0];
            }

            if (StringUtils.hasText(val) && !ownerName.equals(val)) {
                arguments.put("value", "'" + val + "'");
            }
            if (this.annotationType().equals(RequestMapping.class)) {
                arguments.put("method", "RequestMethod." + this.getRequestMethod().name());
            }

            //客户端和服务的produces consumes 逻辑对调
            String[] consumes = this.consumes();
            if (consumes.length > 0) {

                //尝试转化
                String consume = consumes[0];
                String produces = MEDIA_TYPE_MAPPING.get(consume);
                if (consume == null) {
                    produces = JSON.toJSONString(produces);
                } else {
                    produces = "[" + produces + "]";
                }
                arguments.put("produces", produces);
            }

            String[] produces = this.produces();
            if (produces.length > 0) {

                //尝试转化
                String produce = produces[0];
                String _consumes = MEDIA_TYPE_MAPPING.get(produce);
                if (produce == null) {
                    _consumes = JSON.toJSONString(produces);
                } else {
                    _consumes = "[" + _consumes + "]";
                }
                arguments.put("consumes", _consumes);
            }

            codeGenAnnotation.setNamedArguments(arguments);

            //注解位置参数
            List<String> positionArguments = new LinkedList<>();
            positionArguments.add(arguments.get("value"));
            positionArguments.add(arguments.get("method"));
            positionArguments.add(arguments.get("produces"));
            positionArguments.add(arguments.get("produces"));

            codeGenAnnotation.setPositionArguments(positionArguments);

            return codeGenAnnotation;
        }

        /**
         * 获取请求方法
         *
         * @return
         */
        protected RequestMethod getRequestMethod() {

            RequestMethod[] requestMethods = this.method();

            if (requestMethods.length == 0) {
                return RequestMethod.POST;
            } else {
                return requestMethods[0];
            }
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
