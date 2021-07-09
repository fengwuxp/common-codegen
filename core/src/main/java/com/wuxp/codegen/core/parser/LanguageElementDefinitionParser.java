package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.core.parser.enhance.SimpleLanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonBaseMeta;
import org.springframework.lang.Nullable;

import java.util.Optional;

/**
 * 用于将 {@link Class<?>} 对象解析为代码生成的模型对象
 *
 * @author wuxp
 */
public interface LanguageElementDefinitionParser<C extends CommonBaseMeta, S>
        extends SimpleLanguageDefinitionPostProcessor<C>,
        LanguageElementDefinitionDispatcher,
        LanguageElementDefinitionFactory<C> {

    /**
     * 解析一个 element
     *
     * @param source 待解析 {@link S} 类型 的 element
     * @return 解析结果
     * @see #parseOfNullable
     */
    @Nullable
    C parse(S source);

    /**
     * @param source 待解析 {@link S} 类型 的 element
     * @return parse result
     * @see #parse
     */
    default Optional<C> parseOfNullable(S source) {
        return Optional.ofNullable(parse(source));
    }

}
