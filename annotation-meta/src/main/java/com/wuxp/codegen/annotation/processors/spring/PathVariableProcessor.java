package com.wuxp.codegen.annotation.processors.spring;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.NamedAnnotationMate;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
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


    public abstract static class PathVariableMate implements NamedAnnotationMate, PathVariable {

        protected final PathVariable pathVariable;

        public PathVariableMate(PathVariable pathVariable) {
            this.pathVariable = pathVariable;
        }

        @Override
        public String name() {
            return pathVariable.name();
        }

        @Override
        public String value() {
            return pathVariable.value();
        }

        @Override
        public CommonCodeGenAnnotation toAnnotation(Parameter annotationOwner) {
            CommonCodeGenAnnotation annotation = new CommonCodeGenAnnotation();
            annotation.setName(PathVariable.class.getSimpleName());
            Map<String, String> arguments = new LinkedHashMap<>();
            String value = getParameterName(annotationOwner);
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

            return String.format("参数：%s是一个路径参数, %s", this.getParameterName(annotationOwner), required() ? "必填" : "非必填");

        }
    }
}
