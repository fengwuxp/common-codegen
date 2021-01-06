package com.wuxp.codegen.core.strategy;

/**
 * 包名映射策略
 */
public interface PackageMapStrategy {


  /**
   * @param clazz
   * @return 导入该类的导入语句字符串
   */
  String convert(Class<?> clazz);


  /**
   * 转换 class Name
   *
   * @param clazz
   * @return
   */
  String convertClassName(Class<?> clazz);


  /**
   * 生成包名路径
   *
   * @param uris
   * @return
   */
  String genPackagePath(String[] uris);
}
