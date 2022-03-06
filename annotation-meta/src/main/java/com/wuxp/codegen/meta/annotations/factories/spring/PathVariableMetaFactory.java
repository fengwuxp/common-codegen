package com.wuxp.codegen.meta.annotations.factories.spring;

import com.wuxp.codegen.meta.annotations.factories.AbstractAnnotationMetaFactory;
import com.wuxp.codegen.meta.annotations.factories.AbstractNamedAnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Parameter;

/**
 * @author wxup
 * @see PathVariable
 * 处理 PathVariable 注解
 */
public class PathVariableMetaFactory extends AbstractAnnotationMetaFactory<PathVariable, PathVariableMetaFactory.PathVariableMate> {


    @Override
    public PathVariableMetaFactory.PathVariableMate factory(PathVariable annotation) {
        return super.newProxyMate(annotation, PathVariableMetaFactory.PathVariableMate.class);
    }

    public abstract static class PathVariableMate extends AbstractNamedAnnotationMate implements NamedAnnotationMate, PathVariable {

        @Override
        public String toComment(Parameter annotationOwner) {
            return String.format("参数：%s是一个路径参数, %s", this.getParameterName(annotationOwner), required() ? "必填" : "非必填");
        }
    }
}
