package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Parameter;


/**
 * swagger3 注解处理
 *
 * @author wuxp
 * @see Schema
 */
public class SchemaMetaFactory extends AbstractAnnotationMetaFactory<Schema, SchemaMetaFactory.OperationMate> {


    @Override
    public OperationMate factory(Schema annotation) {
        return this.newProxyMate(annotation, OperationMate.class);
    }

    public abstract static class OperationMate implements AnnotationMate, Schema {

        @Override
        public String toComment(AnnotatedElement element) {
            String defaultValue = defaultValue();
            if (!StringUtils.hasText(defaultValue)) {
                defaultValue = RequestMappingUtils.findRequestParam(element).map(RequestParam::defaultValue).orElse("");
            }
            String name = name();
            if (element instanceof Parameter) {
                name = getParameterName((Parameter) element, name);
            }
            return String.format("%s %s %s，默认值：%s，示例输入：%s", name, title(), description(), defaultValue, this.example());
        }
    }
}
