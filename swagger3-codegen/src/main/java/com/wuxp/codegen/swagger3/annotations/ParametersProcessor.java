package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processors.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processors.AnnotationMate;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * swagger3 注解处理
 *
 * @author wuxp
 * @see Parameters
 */
public class ParametersProcessor extends AbstractAnnotationProcessor<Parameters, ParametersProcessor.ParametersMate> {


    @Override
    public ParametersMate process(Parameters annotation) {
        return this.newProxyMate(annotation, ParametersMate.class);
    }

    public abstract static class ParametersMate implements AnnotationMate, Parameters {

        private final Parameters parameters;

        private final ParameterProcessor parameterProcessor = new ParameterProcessor();

        ParametersMate(Parameters parameters) {
            this.parameters = parameters;
        }

        @Override
        public Parameter[] value() {
            return getApiImplicitParams();
        }

        @Override
        public String toComment(Method annotationOwner) {
            Parameter[] value = getApiImplicitParams();
            List<String> comments = new ArrayList<>();
            comments.add("<pre>");
            comments.add("参数列表：");
            comments.addAll(Arrays.stream(value)
                    .map(item -> {
                        ParameterProcessor.ParameterMate mate = (ParameterProcessor.ParameterMate) item;
                        return String.format("参数名称：%s，参数说明：%s", mate.name(), mate.toComment(annotationOwner));
                    }).collect(Collectors.toList()));
            comments.add("</pre>");
            return String.join(MULTILINE_COMMENT_TAG, comments);
        }

        private Parameter[] getApiImplicitParams() {
            Parameter[] value = parameters.value();
            return Arrays.stream(value)
                    .map(parameterProcessor::process)
                    .toArray(Parameter[]::new);
        }

    }
}
