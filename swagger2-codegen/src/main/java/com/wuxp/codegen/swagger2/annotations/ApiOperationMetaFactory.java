package com.wuxp.codegen.swagger2.annotations;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * swagger2 注解处理
 *
 * @see ApiOperation
 */
public class ApiOperationMetaFactory extends AbstractAnnotationMetaFactory<ApiOperation, ApiOperationMetaFactory.ApiOperationMeta> {


    @Override
    public ApiOperationMeta factory(ApiOperation annotation) {
        return this.newProxyMate(annotation, ApiOperationMeta.class);
    }

    public abstract static class ApiOperationMeta implements AnnotationMate, ApiOperation {


        @Override
        public String toComment(Field annotationOwner) {
            return this.value();
        }

        @Override
        public String toComment(Method annotationOwner) {
            return this.value();
        }
    }
}
