package com.wuxp.codegen.core;

import java.util.Set;

/**
 * 用于探测统一的响应对象
 *
 * @author wuxp
 */
public interface UnifiedResponseExplorer {

    /**
     * 探测 统一下响应对象
     *
     * @param classes 需要生成服务类
     */
    void probe(Set<Class<?>> classes);

}
