package com.wuxp.codegen.annotation.processor.spring;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
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
public class RequestMappingProcessor extends AbstractAnnotationProcessor<Annotation, RequestMappingProcessor.RequestMappingMate> {


    //Mapping和mapping元数据的对应
    private static final Map<Class<? extends Annotation>, Class<? extends RequestMappingMate>> ANNOTATION_CLASS_MAP = new LinkedHashMap<>();

    //请求方法和Mapping名称的对应
    private static final Map<RequestMethod, String> METHOD_MAPPING_NAME_MAP = new HashMap<>();


    //媒体类型映射
    private static final Map<String, String> MEDIA_TYPE_MAPPING = new LinkedHashMap<>();

    static {
        ANNOTATION_CLASS_MAP.put(RequestMapping.class, RequestMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PostMapping.class, PostMappingMate.class);
        ANNOTATION_CLASS_MAP.put(GetMapping.class, GetMappingMate.class);
        ANNOTATION_CLASS_MAP.put(DeleteMapping.class, DeleteMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PutMapping.class, PutMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PatchMapping.class, PatchMappingMate.class);

        METHOD_MAPPING_NAME_MAP.put(RequestMethod.GET, "GetMapping");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.POST, "PostMapping");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.DELETE, "DeleteMapping");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PUT, "PutMapping");
        METHOD_MAPPING_NAME_MAP.put(RequestMethod.PATCH, "PatchMapping");

        MEDIA_TYPE_MAPPING.put(MediaType.MULTIPART_FORM_DATA_VALUE, "MediaType.FORM_DATA");
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_JSON_VALUE, "MediaType.JSON");
        MEDIA_TYPE_MAPPING.put(MediaType.APPLICATION_JSON_UTF8_VALUE, "MediaType.JSON_UTF8");
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

            CommonCodeGenAnnotation codeGenAnnotation = this.genAnnotation(annotationOwner.getSimpleName());
            //将类上的注解改为feign
            codeGenAnnotation.setName("Feign");
            codeGenAnnotation.getPositionArguments().remove("method");
            return codeGenAnnotation;
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Method annotationOwner) {

            return this.genAnnotation(annotationOwner.getName());
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

        /**
         * 生成 RequestMapping 相关注解
         *
         * @param ownerName 所有者的name 如果ownerName和value中的一致，则不生成value
         * @return
         */
        private CommonCodeGenAnnotation genAnnotation(String ownerName) {
            CommonCodeGenAnnotation codeGenAnnotation = new CommonCodeGenAnnotation();
            codeGenAnnotation.setName(this.annotationType().getSimpleName());

            //注解命名参数
            Map<String, String> arguments = new LinkedHashMap<>();
            String[] value = this.value();
            String val = null;
            if (value.length > 0) {
                val = value[0];
            }

            //如果val不存在或者ownerName和value中的一致，则不生成value
            if (StringUtils.hasText(val) && !ownerName.equals(val)) {
                arguments.put("value", MessageFormat.format("''{0}''", val));
            }
            if (this.annotationType().equals(RequestMapping.class)) {
//                arguments.put("method", "RequestMethod." + this.getRequestMethod().name());
                //将RequestMapping 转换为其他明确的Mapping类型
                RequestMethod requestMethod = this.getRequestMethod();
                if (requestMethod == null) {
                    //默认的为GET
                    requestMethod = RequestMethod.GET;
                }
                String name = METHOD_MAPPING_NAME_MAP.get(requestMethod);
                codeGenAnnotation.setName(name);
            }


            Map<String, String[]> mediaTypes = new HashMap<>();
            //在注解中属性名称
            String[] attrNames = {"produces", "consumes"};
            //客户端和服务的produces consumes 逻辑对调
            mediaTypes.put(attrNames[0], this.consumes());
            mediaTypes.put(attrNames[1], this.produces());

            for (Map.Entry<String, String[]> entry : mediaTypes.entrySet()) {
                //尝试转化
                String[] entryValue = entry.getValue();
                if (entryValue.length == 0) {
                    continue;
                }
                String mediaType = entryValue[0];

                if (mediaType == null) {
                    continue;
                }
                //如果是json 则不生成，json 是默认策略
                if (MediaType.APPLICATION_JSON_VALUE.equals(mediaType)) {
                    continue;
                }

                String _mediaType = MEDIA_TYPE_MAPPING.get(mediaType);
                if (_mediaType == null) {
                    throw new RuntimeException("unsupported media type：" + mediaType);
                }
                _mediaType = "[" + _mediaType + "]";
                arguments.put(entry.getKey(), _mediaType);
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
