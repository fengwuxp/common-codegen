package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * swagger3 注解处理
 *
 * @author wuxp
 * @see Parameter
 */
public class ParameterProcessor extends AbstractAnnotationProcessor<Parameter, ParameterProcessor.ParameterMate> {


    @Override
    public ParameterMate process(Parameter annotation) {
        return this.newProxyMate(annotation, ParameterMate.class);
    }

    public abstract static class ParameterMate implements AnnotationMate, Parameter {


        @Override
        public String toComment(Class<?> annotationOwner) {
            return this.getDescription();
        }

        @Override
        public String toComment(Field annotationOwner) {
            return this.getDescription();
        }

        @Override
        public String toComment(Method annotationOwner) {
            return this.getDescription();
        }

        private String getDescription() {
            String desc = this.description();
            String name = this.name();
            String description = StringUtils.hasText(desc) ? desc : name;
            return String.format("属性名称：%s，属性说明：%s，示例输入：%s", name, description, this.example());
        }
    }
}
