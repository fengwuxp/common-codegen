package com.wuxp.codegen.meta.annotations.retrofit2;

import com.wuxp.codegen.meta.annotations.AbstractClientAnnotationProvider;
import com.wuxp.codegen.meta.annotations.factories.spring.*;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.*;

import java.lang.annotation.ElementType;
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

    public Retrofit2AnnotationProvider() {
        super(new ConcurrentHashMap<>(8));
        addAnnotation(RequestBody.class, RetrofitRequestBodyMate.class);
        addAnnotation(RequestHeader.class, RetrofitRequestHeaderMate.class);
        addAnnotation(RequestParam.class, RetrofitRequestParamMate.class);
        addAnnotation(PathVariable.class, RetrofitPathVariableMate.class);
        addAnnotation(CookieValue.class, RetrofitCookieValueMate.class);
    }

    public abstract static class RetrofitRequestBodyMate extends RequestBodyMetaFactory.RequestBodyMate {

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(Body.class.getSimpleName());
            annotation.setNamedArguments(Collections.emptyMap());
            annotation.setElementType(ElementType.PARAMETER);
            return annotation;
        }
    }

    public abstract static class RetrofitRequestHeaderMate extends RequestHeaderMetaFactory.RequestHeaderMate {

        protected RetrofitRequestHeaderMate(RequestHeader requestHeader) {
            super(requestHeader);
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = super.toAnnotation(annotationOwner);
            annotation.setName(Header.class.getSimpleName());
            removeNameToValue(annotation.getNamedArguments());
            annotation.setElementType(ElementType.PARAMETER);
            return annotation;
        }
    }

    public abstract static class RetrofitRequestParamMate extends RequestParamMetaFactory.RequestParamMate {

        protected RetrofitRequestParamMate(RequestParam requestParam) {
            super(requestParam);
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            Method method = (Method) annotationOwner.getDeclaringExecutable();
            Optional<RequestMappingMetaFactory.RequestMappingMate> requestMappingAnnotation = RequestMappingUtils.findRequestMappingAnnotation(method.getAnnotations());
            if (!requestMappingAnnotation.isPresent()) {
                return null;
            }
            RequestMappingMetaFactory.RequestMappingMate requestMappingMate = requestMappingAnnotation.get();
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
            annotation.setElementType(ElementType.PARAMETER);
            return annotation;
        }
    }

    public abstract static class RetrofitPathVariableMate extends PathVariableMetaFactory.PathVariableMate {

        protected RetrofitPathVariableMate(PathVariable pathVariable) {
            super(pathVariable);
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = super.toAnnotation(annotationOwner);
            annotation.setName(Path.class.getSimpleName());
            removeNameToValue(annotation.getNamedArguments());
            annotation.setElementType(ElementType.PARAMETER);
            return annotation;
        }
    }

    public abstract static class RetrofitCookieValueMate extends CookieValueMetaFactory.CookieValueMate {

        protected RetrofitCookieValueMate(CookieValue cookieValue) {
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
            annotation.setElementType(ElementType.PARAMETER);
            return annotation;
        }
    }


    private static void removeNameToValue(Map<String, String> namedArguments) {
        String name = namedArguments.remove("name");
        namedArguments.put("value", name);
    }

}

