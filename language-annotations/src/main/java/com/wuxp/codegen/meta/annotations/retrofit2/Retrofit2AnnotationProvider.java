package com.wuxp.codegen.meta.annotations.retrofit2;

import com.wuxp.codegen.meta.annotations.AbstractClientAnnotationProvider;
import com.wuxp.codegen.meta.annotations.factories.spring.CookieValueMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.PathVariableMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestBodyMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestHeaderMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestParamMetaFactory;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.wuxp.codegen.meta.annotations.factories.AnnotationMate.ANNOTATION_NAME_KEY;
import static com.wuxp.codegen.meta.annotations.factories.AnnotationMate.ANNOTATION_VALUE_KEY;

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
            boolean isUseQueryString = RequestMethod.GET.equals(requestMethod) || RequestMethod.DELETE.equals(requestMethod);
            removeNameToValue(annotation.getNamedArguments());
            Map<String, String> namedArguments = new HashMap<>();
            if (isUseQueryString) {
                annotation.setName(Query.class.getSimpleName());
                return annotation;
            }
            boolean isSupportBody = RequestMethod.POST.equals(requestMethod) ||
                    RequestMethod.PUT.equals(requestMethod) || RequestMethod.PATCH.equals(requestMethod);
            if (isSupportBody) {
                annotation.setName(Field.class.getSimpleName());
            }
            annotation.setNamedArguments(namedArguments);
            annotation.setElementType(ElementType.PARAMETER);
            return annotation;
        }
    }

    public abstract static class RetrofitPathVariableMate extends PathVariableMetaFactory.PathVariableMate {

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = super.toAnnotation(annotationOwner);
            annotation.setName(Path.class.getSimpleName());
            Map<String, String> namedArguments = annotation.getNamedArguments();
            namedArguments.remove("required");
            removeNameToValue(namedArguments);
            return annotation;
        }
    }

    public abstract static class RetrofitCookieValueMate extends CookieValueMetaFactory.CookieValueMate {

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = super.toAnnotation(annotationOwner);
            annotation.setName(Header.class.getSimpleName());
            Map<String, String> namedArguments = annotation.getNamedArguments();
            removeNameToValue(namedArguments);
            // 需要接入方实现
            String value = "cookie@" + namedArguments.get(ANNOTATION_VALUE_KEY);
            namedArguments.put("value", value);
            annotation.setElementType(ElementType.PARAMETER);
            return annotation;
        }
    }

    private static void removeNameToValue(Map<String, String> namedArguments) {
        namedArguments.put(ANNOTATION_VALUE_KEY, namedArguments.remove(ANNOTATION_NAME_KEY));
    }

}

