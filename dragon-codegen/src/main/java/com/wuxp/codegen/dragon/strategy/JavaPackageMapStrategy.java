package com.wuxp.codegen.dragon.strategy;

import com.wuxp.codegen.core.strategy.AbstractPackageMapStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author wuxp
 */
@Slf4j
public class JavaPackageMapStrategy extends AbstractPackageMapStrategy {


  private final String basePackage;

  public JavaPackageMapStrategy(Map<String, String> packageNameMap, String basePackage) {
    super(packageNameMap);
    this.basePackage = basePackage;
  }


  public JavaPackageMapStrategy(Map<String, String> packageNameMap, Map<String, Object> classNameTransformers, String basePackage) {
    super(packageNameMap, classNameTransformers);
    this.basePackage = basePackage;
  }

  public JavaPackageMapStrategy(Map<String, String> packageNameMap, Map<String, Object> classNameTransformers, String fileNamSuffix,
      String basePackage) {
    super(packageNameMap, classNameTransformers, fileNamSuffix);
    this.basePackage = basePackage;
  }

  @Override
  public String convert(Class<?> clazz) {
    String path = super.convert(clazz);
    if (!StringUtils.hasText(path)) {
      log.warn("{}转换后的导入的路径为空", clazz.getName());
      return clazz.getSimpleName();
    }
    return this.controllerToService(path);

  }

  @Override
  public String genPackagePath(String[] uris) {
    String packageName = String.join(".", uris);
    if (!packageName.startsWith(this.basePackage)) {
      packageName = MessageFormat.format("{0}.{1}", basePackage, packageName);
    }
    return packageName;
  }
}
