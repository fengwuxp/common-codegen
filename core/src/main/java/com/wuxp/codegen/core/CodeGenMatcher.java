package com.wuxp.codegen.core;


/**
 * 用于匹配一个{@link Class} 是否是需要生成的目标
 *
 * @author wxup
 */
@FunctionalInterface
public interface CodeGenMatcher {

  /**
   * 是否匹配
   *
   * @param clazz class data
   * @return <code>true</code> 匹配生成
   */
  boolean match(Class<?> clazz);
}
