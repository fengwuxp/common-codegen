package com.wuxp.codegen.annotation.processor.spring;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wxup
 * @see PathVariable
 * 处理 PathVariable 注解
 */
public class PathVariableProcessor extends AbstractAnnotationProcessor<PathVariable, PathVariableProcessor.PathVariableMate> {


    @Override
    public PathVariableProcessor.PathVariableMate process(PathVariable annotation) {

        return super.newProxyMate(annotation, PathVariableProcessor.PathVariableMate.class);
    }


    public abstract static class PathVariableMate implements AnnotationMate, PathVariable {

        public PathVariableMate() {
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(PathVariable.class.getSimpleName());
            Map<String, String> arguments = new LinkedHashMap<>();
            String value = this.value();
            if (!StringUtils.hasText(value)) {
                value = this.name();
            }
            if (!StringUtils.hasText(value)) {
                value = annotationOwner.getName();
            }
            if (StringUtils.hasText(value)) {
                arguments.put("name", MessageFormat.format("\"{0}\"", value));
            }
            //注解位置参数
            List<String> positionArguments = new LinkedList<>(arguments.values());
            annotation.setNamedArguments(arguments)
                    .setPositionArguments(positionArguments);
            return annotation;
        }

        @Override
        public String toComment(Parameter annotationOwner) {

            return MessageFormat.format("属性：{0}是一个 path variable", annotationOwner.getName());
        }
    }
}
