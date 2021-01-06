package com.wuxp.codegen.core.strategy;


import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import java.lang.reflect.Parameter;

/**
 * 生成的匹配策略
 * <p>
 * 匹配目标（类，或方法）是否需要生成
 * </p>
 *
 * @author wxup
 */
public interface CodeGenMatchingStrategy {

  /**
   * 类是否匹配
   *
   * @param classMeta 类元数据
   * @return <code>return true</code> 需要生成
   */
  boolean isMatchClazz(JavaClassMeta classMeta);

  /**
   * 方法是否匹配
   *
   * @param methodMeta 方法元数据
   * @return <code>return true</code> 需要生成
   */
  boolean isMatchMethod(JavaMethodMeta methodMeta);


  /**
   * 是否匹配类字段
   *
   * @param javaFieldMeta 字段元数据
   * @return <code>return true</code> 需要生成
   */
  boolean isMatchField(JavaFieldMeta javaFieldMeta);

  /**
   * 是否匹配方法参数
   *
   * @param javaMethodMeta 方法元数据
   * @param parameter      参数
   * @return <code>return true</code> 需要生成
   */
  boolean isMatchParameter(JavaMethodMeta javaMethodMeta, Parameter parameter);

}
