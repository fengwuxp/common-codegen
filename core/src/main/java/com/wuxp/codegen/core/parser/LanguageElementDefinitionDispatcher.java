package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonBaseMeta;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wuxp
 */
public interface LanguageElementDefinitionDispatcher {


    /**
     * 分发一个元数据对象用于解析成对应的 {@link CommonBaseMeta} 子类
     */
    default <T extends CommonBaseMeta> Optional<T> dispatchOfNullable(Object meta) {
        return SimpleLanguageElementDefinitionDispatcher.getInstance().dispatchOfNullable(meta);
    }

    /**
     * 分发一个元数据对象用于解析成对应的 {@link CommonBaseMeta} 子类
     */
    default <T extends CommonBaseMeta> T dispatch(Object meta) {
        return SimpleLanguageElementDefinitionDispatcher.getInstance().dispatch(meta);
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
