package com.wuxp.codegen.swagger3.builder;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.core.macth.ExcludeAnnotationCodeGenElementMatcher;
import com.wuxp.codegen.languages.AnnotationMetaFactoryHolder;
import com.wuxp.codegen.swagger3.Swagger3FieldMatcher;
import com.wuxp.codegen.swagger3.Swagger3MethodMatcher;
import com.wuxp.codegen.swagger3.Swagger3ParameterMatcher;
import com.wuxp.codegen.swagger3.annotations.*;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Collections;

public abstract class AbstractSwagger3CodegenBuilder extends AbstractLoongCodegenBuilder {

    @Override
    protected void initAnnotationMetaFactory() {
        // 添加swagger3相关的注解处理器
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(Operation.class, new OperationMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiResponse.class, new ApiResponseMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(Parameter.class, new ParameterMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(Parameters.class, new ParametersMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(RequestBody.class, new RequestBodyMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(Schema.class, new SchemaMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(Tag.class, new TagMetaFactory());
    }

    @Override
    protected void configCodeGenElementMatchers() {
        super.configCodeGenElementMatchers();
        this.codeGenElementMatchers(
                new ExcludeAnnotationCodeGenElementMatcher(Collections.singletonList(Hidden.class)),
                new Swagger3MethodMatcher(),
                new Swagger3FieldMatcher(),
                new Swagger3ParameterMatcher()
        );
    }
}
