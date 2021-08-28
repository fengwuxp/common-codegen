package com.wuxp.codegen.swagger2.builder;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.core.macth.ExcludeAnnotationCodeGenElementMatcher;
import com.wuxp.codegen.core.macth.JavaClassElementMatcher;
import com.wuxp.codegen.core.macth.JavaFiledElementMatcher;
import com.wuxp.codegen.core.macth.JavaMethodElementMatcher;
import com.wuxp.codegen.languages.AnnotationMetaFactoryHolder;
import com.wuxp.codegen.languages.RemoveClientResponseTypePostProcessor;
import com.wuxp.codegen.languages.typescript.EnumNamesPostProcessor;
import com.wuxp.codegen.meta.enums.EnumDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
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

    protected void configCodeGenElementMatchers() {
        this.codeGenElementMatchers(
                new ExcludeAnnotationCodeGenElementMatcher(Collections.singletonList(ApiIgnore.class)),
                JavaClassElementMatcher.builder()
                        .includePackages(this.getIncludePackages())
                        .includeClasses(this.getIncludeClasses())
                        .includePackages(this.getIgnorePackages())
                        .ignoreClasses(this.getIgnoreClasses())
                        .build(),
                new JavaMethodElementMatcher(this.getIgnoreMethodNames()),
                new JavaFiledElementMatcher(this.getIgnoreFieldNames())
        );
    }

    protected void configParserPostProcessors(CommonCodeGenClassMeta clientResponseType) {
        this.elementParsePostProcessors(
                new RemoveClientResponseTypePostProcessor(clientResponseType),
                new EnumDefinitionPostProcessor(),
                new EnumNamesPostProcessor()
        );
    }

}
