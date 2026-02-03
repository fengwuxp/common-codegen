package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonBaseMeta;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 通过事件解析不同语言定义的解析器
 *
 * @author wuxp
 */
public interface LanguageElementDefinitionPublishSourceParser {

    default <C extends CommonBaseMeta> C publishParse(Object source) {
        throw new UnsupportedOperationException("un support publishParse");
    }

    /**
     * 分发一个元数据对象用于解析成对应的 {@link CommonBaseMeta} 子类
     */
    default <C extends CommonBaseMeta> Optional<C> publishParseOfNullable(Object meta) {
        return Optional.ofNullable(publishParse(meta));
    }

    @SuppressWarnings("unchecked")
    default <C extends CommonBaseMeta> List<C> publishParse(Collection<?> metas) {
        return metas.stream()
                .map(this::publishParse)
                .filter(Objects::nonNull)
                .map(result -> (C) result)
                .toList();
    }

}
