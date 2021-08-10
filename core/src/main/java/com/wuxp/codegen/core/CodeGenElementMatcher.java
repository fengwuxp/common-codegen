package com.wuxp.codegen.core;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 匹配用于生成代码 元素
 *
 * @author wuxp
 */
@FunctionalInterface
public interface CodeGenElementMatcher<S> extends CodeGenClassSupportHandler {

    /**
     * 类是否匹配
     *
     * @param source {@link S} Object
     * @return <code>return true</code> 是否匹配
     */
    boolean matches(S source);

    @Override
    default boolean supports(Class<?> clazz) {
        return false;
    }

    default List<S> filter(Collection<S> sources) {
        return sources.stream()
                .filter(this::matches)
                .collect(Collectors.toList());
    }
}
