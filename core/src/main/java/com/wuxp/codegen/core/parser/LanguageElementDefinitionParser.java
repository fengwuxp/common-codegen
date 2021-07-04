package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.core.parser.enhance.SimpleLanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonBaseMeta;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用于将 {@link Class<?>} 对象解析为代码生成的模型对象
 *
 * @author wuxp
 */
public interface LanguageElementDefinitionParser<C extends CommonBaseMeta, S> extends SimpleLanguageDefinitionPostProcessor<C> {


    /**
     * parse source
     *
     * @param source java AnnotatedElement Object
     * @return parse result
     */
    default Optional<C> parseOfNullable(S source) {
        return Optional.ofNullable(parse(source));
    }

    @Nullable
    C parse(S source);

    /**
     * @return 实例化一个 Element Meta Object
     */
    C newInstance();

    /**
     * 分发一个元数据对象用于解析成对应的 {@link CommonBaseMeta} 子类
     */
    default <T extends CommonBaseMeta> Optional<T> dispatchOfNullable(Object meta) {
        return DispatchLanguageElementDefinitionParser.getInstance().dispatchOfNullable(meta);
    }

    /**
     * 分发一个元数据对象用于解析成对应的 {@link CommonBaseMeta} 子类
     */
    default <T extends CommonBaseMeta> T dispatch(Object meta) {
        return DispatchLanguageElementDefinitionParser.getInstance().dispatch(meta);
    }


    @SuppressWarnings("unchecked")
    default <T extends CommonBaseMeta> List<T> dispatch(Collection<?> metas) {
        return metas.stream()
                .map(this::dispatch)
                .filter(Objects::nonNull)
                .map(result -> (T) result)
                .collect(Collectors.toList());
    }

}
