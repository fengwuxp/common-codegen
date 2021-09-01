package com.wuxp.codegen.swagger2.builder;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.core.macth.ExcludeAnnotationCodeGenElementMatcher;
import com.wuxp.codegen.languages.AnnotationMetaFactoryHolder;
import com.wuxp.codegen.swagger2.Swagger2FieldMatcher;
import com.wuxp.codegen.swagger2.Swagger2MethodMatcher;
import com.wuxp.codegen.swagger2.Swagger2ParameterMatcher;
import com.wuxp.codegen.swagger2.annotations.*;
import io.swagger.annotations.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;

public abstract class AbstractSwagger2CodegenBuilder extends AbstractLoongCodegenBuilder {

    @Override
    protected void initAnnotationMetaFactory() {
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(Api.class, new ApiMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiModel.class, new ApiModelMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiModelProperty.class, new ApiModelPropertyMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiOperation.class, new ApiOperationMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiParam.class, new ApiParamMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiImplicitParam.class, new ApiImplicitParamMetaFactory());
        AnnotationMetaFactoryHolder.registerAnnotationMetaFactory(ApiImplicitParams.class, new ApiImplicitParamsMetaFactory());
    }

    @Override
    protected void configCodeGenElementMatchers() {
        super.configCodeGenElementMatchers();
        this.codeGenElementMatchers(new ExcludeAnnotationCodeGenElementMatcher(Collections.singletonList(ApiIgnore.class)),
                new Swagger2MethodMatcher(),
                new Swagger2FieldMatcher(),
                new Swagger2ParameterMatcher()
        );
    }
}
