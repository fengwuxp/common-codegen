package com.wuxp.codegen.model.mapping;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @param <T>
 * @author wuxp
 */
public abstract class AbstractTypeMapping<T> implements TypeMapping<Class<?>, List<T>> {


  protected final Map<Class<?>, CommonCodeGenClassMeta> baseTypeMappingMap = new LinkedHashMap<>();

  protected final Map<Class<?>, CommonCodeGenClassMeta> customizeTypeMappingMap = new LinkedHashMap<>();

  protected final Map<Class<?>, Class<?>[]> customizeJavaMappingMap = new LinkedHashMap<>();


  public AbstractTypeMapping<T> setBaseTypeMapping(Class<?> clazz, CommonCodeGenClassMeta codeGenClassMeta, boolean forceOverride) {
    if (baseTypeMappingMap.containsKey(clazz) && !forceOverride) {
      return this;
    }
    baseTypeMappingMap.put(clazz, codeGenClassMeta);
    return this;
  }

  public AbstractTypeMapping<T> setBaseTypeMapping(Class<?> clazz, CommonCodeGenClassMeta codeGenClassMeta) {
    return setBaseTypeMapping(clazz, codeGenClassMeta, false);
  }


  public AbstractTypeMapping<T> setCustomizeTypeMapping(Class<?> clazz, CommonCodeGenClassMeta codeGenClassMeta) {
    return setCustomizeTypeMapping(clazz, codeGenClassMeta, false);
  }

  public AbstractTypeMapping<T> setCustomizeTypeMapping(Class<?> clazz, CommonCodeGenClassMeta codeGenClassMeta, boolean forceOverride) {
    if (customizeTypeMappingMap.containsKey(clazz) && !forceOverride) {
      return this;
    }
    customizeTypeMappingMap.put(clazz, codeGenClassMeta);
    return this;
  }

  public AbstractTypeMapping<T> setCustomizeJavaTypeMapping(Class<?> clazz, Class<?>[] classes) {
    return setCustomizeJavaTypeMapping(clazz, classes, false);
  }

  public AbstractTypeMapping<T> setCustomizeJavaTypeMapping(Class<?> clazz, Class<?>[] classes, boolean forceOverride) {
    if (customizeJavaMappingMap.containsKey(clazz) && !forceOverride) {
      return this;
    }
    customizeJavaMappingMap.put(clazz, classes);
    return this;
  }

}
