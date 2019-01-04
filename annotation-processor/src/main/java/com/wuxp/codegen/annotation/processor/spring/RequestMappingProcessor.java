package com.wuxp.codegen.annotation.processor.spring;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.web.bind.annotation.*;
import sun.plugin.javascript.navig.LinkArray;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    static {
        ANNOTATION_CLASS_MAP.put(RequestMapping.class, RequestMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PostMapping.class, PostMappingMate.class);
        ANNOTATION_CLASS_MAP.put(GetMapping.class, GetMappingMate.class);
        ANNOTATION_CLASS_MAP.put(DeleteMapping.class, DeleteMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PutMapping.class, PutMappingMate.class);
        ANNOTATION_CLASS_MAP.put(PatchMapping.class, PatchMappingMate.class);
    }


    @Override
    public RequestMappingMate process(Annotation annotation) {

        Class<? extends RequestMappingMate> clazz = ANNOTATION_CLASS_MAP.get(annotation.annotationType());
        if (clazz == null) {
            throw new RuntimeException("not spring mapping annotation，annotation name: " + annotation.annotationType().getName());
        }

        return this.newProxyMate(annotation, clazz);

    }


    public static abstract class RequestMappingMate implements AnnotationMate<Annotation>, RequestMapping {

        @Override
        public String toComment() {
            return "接口的请求方法为：" + this.method()[0].name();
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation() {
            CommonCodeGenAnnotation codeGenAnnotation = new CommonCodeGenAnnotation();
            codeGenAnnotation.setName(this.annotationType().getSimpleName());

            //注解命名参数
            Map<String, String> arguments = new LinkedHashMap<>();
            arguments.put("value", this.value()[0]);
            arguments.put("method", "RequestMethod." + this.method()[0].name());
            codeGenAnnotation.setNamedArguments(arguments);

            //注解w位置参数
            List<String> positionArguments = new LinkedList<>();
            positionArguments.add(arguments.get("value"));
            positionArguments.add(arguments.get("method"));
            codeGenAnnotation.setPositionArguments(positionArguments);

            return codeGenAnnotation;
        }
    }

    public static abstract class GetMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.GET};

        @Override
        public RequestMethod[] method() {
            return methods;
        }

    }

    public static abstract class PostMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.POST};

        @Override
        public RequestMethod[] method() {
            return methods;
        }
    }

    public static abstract class DeleteMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.DELETE};

        @Override
        public RequestMethod[] method() {
            return methods;
        }

    }

    public static abstract class PutMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.PUT};

        @Override
        public RequestMethod[] method() {
            return methods;
        }

    }

    public static abstract class PatchMappingMate extends RequestMappingMate {

        protected RequestMethod[] methods = new RequestMethod[]{RequestMethod.PATCH};

        @Override
        public RequestMethod[] method() {
            return methods;
        }

    }
}
