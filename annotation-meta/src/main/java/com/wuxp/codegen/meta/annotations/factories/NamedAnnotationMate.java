package com.wuxp.codegen.meta.annotations.factories;

import com.wuxp.codegen.core.parser.JavaClassParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.Parameter;

/**
 * 有名称属性的注解，且value属性和名称属性的意思一致
 *
 * @author wuxp
 */
public interface NamedAnnotationMate extends AnnotationMate {

    default String name() {
        return "";
    }

    /**
     * 获取参数的名称
     *
     * @param parameter 被注解标记的参数
     * @return 参数名称
     */
    @Override
    default String getParameterName(Parameter parameter) {
        String name = this.name();
        if (StringUtils.hasText(name)) {
            return name;
        }
        return JavaClassParser.getParameterName(parameter);
    }
}
