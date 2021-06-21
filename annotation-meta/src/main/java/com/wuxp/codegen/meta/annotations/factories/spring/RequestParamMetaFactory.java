package com.wuxp.codegen.meta.annotations.factories.spring;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wxup
 * @see RequestParam
 * 处理 RequestParam 注解
 */
public class RequestParamMetaFactory extends AbstractAnnotationMetaFactory<RequestParam, RequestParamMetaFactory.RequestParamMate> {


    @Override
    public RequestParamMetaFactory.RequestParamMate factory(RequestParam annotation) {

        return super.newProxyMate(annotation, RequestParamMetaFactory.RequestParamMate.class);
    }


    public abstract static class RequestParamMate implements NamedAnnotationMate, RequestParam {

        protected final RequestParam requestParam;

        protected RequestParamMate(RequestParam requestParam) {
            this.requestParam = requestParam;
        }

        @Override
        public String name() {
            return requestParam.name();
        }

        @Override
        public String value() {
            return requestParam.value();
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(RequestParam.class.getSimpleName());
            Map<String, String> arguments = new LinkedHashMap<>();
            String value = this.getParameterName(annotationOwner);
            arguments.put("name", MessageFormat.format("\"{0}\"", value));
            arguments.put("required", this.required() + "");
            //注解位置参数
            List<String> positionArguments = new LinkedList<>(arguments.values());
            annotation.setNamedArguments(arguments)
                    .setPositionArguments(positionArguments);
            return annotation;
        }

        @Override
        public String toComment(Parameter annotationOwner) {

            return getRequestAnnotationDesc(String.format("参数：%s是一个查询参数或表单参数，%s", this.getParameterName(annotationOwner), required() ? "必填" : "非必填"), defaultValue());
        }
    }

    public static String getRequestAnnotationDesc(String desc, String defaultValue) {
        if (!StringUtils.hasText(getRequestAnnotationDesc(defaultValue))) {
            return desc;
        }
        return desc + "，默认值：" + defaultValue;
    }

    public static String getRequestAnnotationDesc(String defaultValue) {
        if (ValueConstants.DEFAULT_NONE.equals(defaultValue)) {
            return "";
        }
        return defaultValue;
    }
}
