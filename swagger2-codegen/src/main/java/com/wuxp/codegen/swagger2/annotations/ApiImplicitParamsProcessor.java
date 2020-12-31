package com.wuxp.codegen.swagger2.annotations;

import com.wuxp.codegen.annotation.processor.AbstractAnnotationProcessor;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.model.LanguageDescription;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * swagger2 注解处理
 *
 * @author wuxp
 * @see ApiImplicitParams
 */
public class ApiImplicitParamsProcessor extends AbstractAnnotationProcessor<ApiImplicitParams, ApiImplicitParamsProcessor.ApiImplicitParamsMate> {


    @Override
    public ApiImplicitParamsMate process(ApiImplicitParams annotation) {
        return this.newProxyMate(annotation, ApiImplicitParamsMate.class);
    }

    public abstract static class ApiImplicitParamsMate implements AnnotationMate, ApiImplicitParams {

        private final ApiImplicitParams apiImplicitParams;

        private final ApiImplicitParamProcessor apiImplicitParamProcessor = new ApiImplicitParamProcessor();

        public ApiImplicitParamsMate(ApiImplicitParams apiImplicitParams) {
            this.apiImplicitParams = apiImplicitParams;
        }

        @Override
        public ApiImplicitParam[] value() {
            return getApiImplicitParams();
        }


        @Override
        public String toComment(Method annotationOwner) {
            ApiImplicitParam[] value = getApiImplicitParams();
            CodegenConfig config = CodegenConfigHolder.getConfig();
            LanguageDescription languageDescription = config.getLanguageDescription();
            String commentSymbol = LanguageDescription.DART.equals(languageDescription) ? "///" : "*";
            return "<pre> \n " + commentSymbol + "参数列表：\r\n" + Arrays.stream(value)
                    .map(item -> {
                        ApiImplicitParamProcessor.ApiImplicitParamMate mate = (ApiImplicitParamProcessor.ApiImplicitParamMate) item;
                        return commentSymbol + "参数名称：" + mate.name() + "，参数说明：" + mate.toComment(annotationOwner) + "\r\n";
                    }).collect(Collectors.joining("")) + commentSymbol + "</pre>";
        }

        private ApiImplicitParam[] getApiImplicitParams() {
            ApiImplicitParam[] value = apiImplicitParams.value();
            return Arrays.stream(value)
                    .map(apiImplicitParamProcessor::process)
                    .toArray(ApiImplicitParam[]::new);
        }
    }
}
