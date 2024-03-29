package com.wuxp.codegen.core;

import java.util.Collection;

/**
 * 用于探测统一的响应对象
 *
 * @author wuxp
 */
public interface UnifiedResponseExplorer {

    /**
     * 探测 统一下响应对象
     *
     * @param apiClasses 需要生成服务类
     */
   void probe(Collection<Class<?>> apiClasses);

}
