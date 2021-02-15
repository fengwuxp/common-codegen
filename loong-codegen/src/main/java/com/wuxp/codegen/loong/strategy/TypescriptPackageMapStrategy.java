package com.wuxp.codegen.loong.strategy;

import com.wuxp.codegen.core.strategy.AbstractPackageMapStrategy;
import com.wuxp.codegen.loong.path.PathResolve;
import com.wuxp.codegen.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Map;


/**
 * typescript包名(路径) 映射策略
 *
 * @author wuxp
 */
@Slf4j
public class TypescriptPackageMapStrategy extends AbstractPackageMapStrategy {


  public TypescriptPackageMapStrategy(Map<String, String> packageNameMap) {
    super(packageNameMap);
  }


  public TypescriptPackageMapStrategy(Map<String, String> packageNameMap, Map<String, Object> classNameTransformers) {
    super(packageNameMap, classNameTransformers);
  }

  public TypescriptPackageMapStrategy(Map<String, String> packageNameMap, Map<String, Object> classNameTransformers, String fileNamSuffix) {
    super(packageNameMap, classNameTransformers, fileNamSuffix);
  }


  @Override
  public String convert(Class<?> clazz) {
    String path = super.convert(clazz);
    if (!StringUtils.hasText(path)) {
      log.warn("{}转换后的导入的路径为空", clazz.getName());
      return clazz.getSimpleName();
    }

    String convertClassName = FileUtils.packageNameToFilePath(this.controllerToService(path));
    if (convertClassName.startsWith(PathResolve.RIGHT_SLASH)) {
      return convertClassName;
    }
    return PathResolve.RIGHT_SLASH + convertClassName;

  }

  @Override
  public String genPackagePath(String[] uris) {
    return MessageFormat.format("/{0}", String.join(PathResolve.RIGHT_SLASH, uris));
  }
}
