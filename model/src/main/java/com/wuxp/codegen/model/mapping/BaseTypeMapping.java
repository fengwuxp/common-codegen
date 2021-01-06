package com.wuxp.codegen.model.mapping;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import java.util.Map;

/**
 * 基础数据的类型映射
 *
 * @author wuxp
 */
public class BaseTypeMapping<T extends CommonCodeGenClassMeta> implements TypeMapping<Class<?>, T> {

  private final Map<Class<?>, CommonCodeGenClassMeta> typeMapping;

  /**
   * 时间类型 希望装换的目标类型
   */
  private final T dateToClassTarget;

  public BaseTypeMapping(Map<Class<?>, CommonCodeGenClassMeta> typeMapping) {
    this(typeMapping, null);
  }

  public BaseTypeMapping(Map<Class<?>, CommonCodeGenClassMeta> typeMapping, T dateToClassTarget) {
    this.typeMapping = typeMapping;
    this.dateToClassTarget = dateToClassTarget;
  }

  @Override
  public T mapping(Class<?>... classes) {
    if (classes == null) {
      return null;
    }
    Class<?> clazz = classes[0];
    if (JavaTypeUtils.isDate(clazz) && this.dateToClassTarget != null) {
      return this.dateToClassTarget;
    }
    return (T) this.typeMapping.get(clazz);
  }
}
