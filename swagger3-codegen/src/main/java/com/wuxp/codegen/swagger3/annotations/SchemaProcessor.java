package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.util.RequestMappingUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


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
                defaultValue = requestParam.get().defaultValue();
            }
            return String.format("名称：%s，属性说明：%s，默认值：%s，示例输入：%s", name(), description(), defaultValue, this.example());
        }


    }
}
