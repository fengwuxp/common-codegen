package com.wuxp.codegen.core;

import java.util.Collection;

/**
 * 匹配用于生成代码 元素
 *
 * @author wuxp
 */
@FunctionalInterface
public interface CodeGenElementMatcher<S> {

    /**
     * 类是否匹配
     *
     * @param source {@link S} Object
     * @return <code>return true</code> 是否匹配
     */
    boolean matches(S source);

    default boolean matches(Collection<S> sources) {
        return sources.stream().anyMatch(this::matches);
    }
}
