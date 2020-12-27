package com.wuxp.codegen.swagger3.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;

import java.lang.reflect.Method;
import java.util.Arrays;
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

        public ParametersMate(Parameters parameters) {
            this.parameters = parameters;
        }

        @Override
        public Parameter[] value() {
            return getApiImplicitParams();
        }

        @Override
        public String toComment(Method annotationOwner) {
            Parameter[] value = getApiImplicitParams();
            return "<pre> \n 参数列表：\r\n" + Arrays.stream(value)
                    .map(item -> {
                        ParameterProcessor.ParameterMate mate = (ParameterProcessor.ParameterMate) item;
                        return "参数名称：" + mate.name() + "，参数说明：" + mate.toComment(annotationOwner) + "\r\n";
                    }).collect(Collectors.joining("")) + "</pre>";
        }

        private Parameter[] getApiImplicitParams() {
            Parameter[] value = parameters.value();
            return Arrays.stream(value)
                    .map(parameterProcessor::process)
                    .toArray(Parameter[]::new);
        }

    }
}
