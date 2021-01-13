package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import com.wuxp.codegen.util.RequestMappingUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Parameter;
import java.util.Optional;

import static com.wuxp.codegen.annotation.processors.spring.RequestParamProcessor.getRequestAnnotationDesc;


/**
 * swagger3 注解处理
 *
 * @author wuxp
 * @see Schema
 */
public class SchemaProcessor extends AbstractAnnotationProcessor<Schema, SchemaProcessor.OperationMate> {


    @Override
    public OperationMate process(Schema annotation) {
        return this.newProxyMate(annotation, OperationMate.class);
    }

    public abstract static class OperationMate implements AnnotationMate, Schema {


        @Override
        public String toComment(Object annotationOwner) {
            String defaultValue = defaultValue();
            Optional<RequestParam> requestParam = RequestMappingUtils.findRequestParam(annotationOwner);
            if (requestParam.isPresent() &&! StringUtils.hasText(defaultValue)) {
                defaultValue = getRequestAnnotationDesc(defaultValue);
            }
            String name = name();
            if (annotationOwner instanceof Parameter) {
                name = getParameterName((Parameter) annotationOwner, name);
            }
            return String.format("名称：%s，属性说明：%s，默认值：%s，示例输入：%s", name, description(), defaultValue, this.example());
        }


    }
}
