package com.wuxp.codegen.annotation.retrofit2;

import com.wuxp.codegen.annotation.AbstractClientAnnotationProvider;
import com.wuxp.codegen.annotation.processors.spring.*;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.util.RequestMappingUtils;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuxp
 */
public class Retrofit2AnnotationProvider extends AbstractClientAnnotationProvider {

    {
        this.annotationMap.put(RequestBody.class, RetrofitRequestBodyMate.class);
        this.annotationMap.put(RequestHeader.class, RetrofitRequestHeaderMate.class);
        this.annotationMap.put(RequestParam.class, RetrofitRequestParamMate.class);
        this.annotationMap.put(PathVariable.class, RetrofitPathVariableMate.class);
        this.annotationMap.put(CookieValue.class, RetrofitCookieValueMate.class);
    }

    public Retrofit2AnnotationProvider() {
        super(new ConcurrentHashMap<>(8));
    }

    public abstract static class RetrofitRequestBodyMate extends RequestBodyProcessor.RequestBodyMate {

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(Body.class.getSimpleName());
            annotation.setNamedArguments(Collections.emptyMap());
            return annotation;
        }
    }

    public abstract static class RetrofitRequestHeaderMate extends RequestHeaderProcessor.RequestHeaderMate {

        public RetrofitRequestHeaderMate(RequestHeader requestHeader) {
            super(requestHeader);
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = super.toAnnotation(annotationOwner);
            annotation.setName(Header.class.getSimpleName());
            removeNameToValue(annotation.getNamedArguments());
            return annotation;
        }
    }

    public abstract static class RetrofitRequestParamMate extends RequestParamProcessor.RequestParamMate {

        public RetrofitRequestParamMate(RequestParam requestParam) {
            super(requestParam);
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            Method method = (Method) annotationOwner.getDeclaringExecutable();
            Optional<RequestMappingProcessor.RequestMappingMate> requestMappingAnnotation = RequestMappingUtils.findRequestMappingAnnotation(method.getAnnotations());
            if (!requestMappingAnnotation.isPresent()) {
                return null;
            }
            RequestMappingProcessor.RequestMappingMate requestMappingMate = requestMappingAnnotation.get();
            RequestMethod requestMethod = requestMappingMate.getRequestMethod();
            CommonCodeGenAnnotation annotation = super.toAnnotation(annotationOwner);
            removeNameToValue(annotation.getNamedArguments());
            boolean isUseQueryString = RequestMethod.GET.equals(requestMethod) || RequestMethod.DELETE.equals(requestMethod);
            if (isUseQueryString) {
                annotation.setName(Query.class.getSimpleName());
                return annotation;
            }
            boolean isSupportBody = RequestMethod.POST.equals(requestMethod) ||
                    RequestMethod.PUT.equals(requestMethod) || RequestMethod.PATCH.equals(requestMethod);
            if (isSupportBody) {
                annotation.setName(Field.class.getSimpleName());
            }
            return annotation;
        }
    }

    public abstract static class RetrofitPathVariableMate extends PathVariableProcessor.PathVariableMate {

        public RetrofitPathVariableMate(PathVariable pathVariable) {
            super(pathVariable);
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = super.toAnnotation(annotationOwner);
            annotation.setName(Path.class.getSimpleName());
            removeNameToValue(annotation.getNamedArguments());
            return annotation;
        }
    }

    public abstract static class RetrofitCookieValueMate extends CookieValueProcessor.CookieValueMate {

        public RetrofitCookieValueMate(CookieValue cookieValue) {
            super(cookieValue);
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = super.toAnnotation(annotationOwner);
            annotation.setName(Header.class.getSimpleName());
            Map<String, String> namedArguments = annotation.getNamedArguments();
            removeNameToValue(namedArguments);
            // 需要接入方实现
            String value = "cookie@" + namedArguments.get("value");
            namedArguments.put("value", value);
            return annotation;
        }
    }


    private static void removeNameToValue(Map<String, String> namedArguments) {
        String name = namedArguments.remove("name");
        namedArguments.put("value", name);
    }

}

