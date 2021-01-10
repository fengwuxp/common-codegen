package com.wuxp.codegen.core;

import java.util.Set;

/**
 * 用于探测统一的响应对象
 *
 * @author wuxp
 */
public interface UnifiedResponseExplorer {

  void probe(Set<Class<?>> classes);

}
