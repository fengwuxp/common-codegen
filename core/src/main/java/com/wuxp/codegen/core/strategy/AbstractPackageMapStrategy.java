package com.wuxp.codegen.core.strategy;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;


/**
 * 抽象的包名映射策略
 *
 * @author wuxp
 */
@Slf4j
@Setter
public abstract class AbstractPackageMapStrategy implements PackageMapStrategy {

  /**
   * @key 类的包名前缀
   * @value output path
   */
  protected Map<String, String> packageNameMap;

  protected PathMatcher pathMatcher = new AntPathMatcher();

  /**
   * 用于替换文件名称的后缀
   */
  protected String fileNamSuffix = "Service";

  /**
   * 类名转换器
   *
   * @see ClassNameTransformer
   */
  protected Map<String/*类名或ant匹配*/, Object/*字符串或 ClassNameTransformer*/> classNameTransformers;


  public AbstractPackageMapStrategy(Map<String, String> packageNameMap) {
    this.packageNameMap = packageNameMap;
  }

  public AbstractPackageMapStrategy(Map<String, String> packageNameMap, Map<String, Object> classNameTransformers) {
    this.packageNameMap = packageNameMap;
    this.classNameTransformers = classNameTransformers;
  }

  public AbstractPackageMapStrategy(Map<String, String> packageNameMap, Map<String, Object> classNameTransformers, String fileNamSuffix) {
    this.packageNameMap = packageNameMap;
    this.classNameTransformers = classNameTransformers;
    this.fileNamSuffix = fileNamSuffix;
  }

  @Override
  public String convert(Class<?> clazz) {
    //包名
    Package aPackage = clazz.getPackage();
    String packageNames = "";
    String clazzName = clazz.getName();
    if (aPackage == null) {
      log.warn("包名为空的类{}", clazzName);
    } else {
      packageNames = aPackage.getName();
    }
    Optional<String> packageNamePrefix = this.packageNameMap.keySet()
        .stream()
        .map(pattern -> {
          if (!this.pathMatcher.isPattern(pattern)) {
            return pattern;
          }
          if (pattern.endsWith("**")) {
            return pattern;
          }
          return MessageFormat.format("{0}**", pattern);
        }).filter(pattern -> {
          if (!this.pathMatcher.isPattern(pattern)) {
            return clazzName.startsWith(pattern);
          } else {
            return this.pathMatcher.match(pattern, clazzName);
          }

        })
        .findFirst();
    //没有找到可以替换的前缀
    if (!packageNamePrefix.isPresent()) {
      // convertClassName
      return this.convertClassName(clazz);
    }

    String key = packageNamePrefix.get();
    String val = this.packageNameMap.get(key);
    if (val == null) {
      val = this.packageNameMap.get(key.substring(0, key.length() - 2));
    }

//        if (!StringUtils.hasText(val)) {
//            throw new RuntimeException(MessageFormat.format("包名：{0} 未找到装换映射关系", clazzName));
//        }

    String value;

    if (this.pathMatcher.isPattern(key)) {
      //TODO 支持 {0}a{2}模式
      //转换为正则表达式
      String[] strings = key.split("\\*\\*");
      String s = clazzName.replaceAll(strings[0], "");
      if (strings.length > 1) {
        s = s.substring(0, s.indexOf(strings[1]));
      } else {
        log.warn("包名替换--> {}, {}", s, Arrays.toString(strings));
      }
      if (val.contains("{0}")) {
        // 替换占位符
        val = MessageFormat.format(val, s + ".");
      }
      value = clazzName.replace(packageNames, val);
    } else {

      value = clazzName.replace(key, val);
    }
    String[] strings = value.split("\\.");
    strings[strings.length - 1] = this.convertClassName(clazz);
    return String.join(".", strings);
  }

  @Override
  public String convertClassName(Class<?> clazz) {

    Map<String, Object> classNameTransformers = this.classNameTransformers;
    String simpleName = clazz.getSimpleName();
    if (classNameTransformers == null) {
      //默认将Controller 转换为Service
      return controllerToService(simpleName);
    }
    Object object = classNameTransformers.get(simpleName);
    if (object == null) {
      return controllerToService(simpleName);
    }
    if (object instanceof String) {
      return object.toString();
    }
    if (object instanceof ClassNameTransformer) {
      return ((ClassNameTransformer) object).transform(clazz);
    }

    PathMatcher pathMatcher = this.pathMatcher;
    String name = clazz.getName();
    // ant 匹配
    for (Map.Entry<String, Object> entry : classNameTransformers.entrySet()) {
      String s = entry.getKey();
      Object transformer = entry.getValue();
      if (pathMatcher.match(s, name)) {
        if (transformer instanceof ClassNameTransformer) {
          return ((ClassNameTransformer) transformer).transform(clazz);
        }
      }
    }

    return simpleName;
  }

  protected String controllerToService(String simpleName) {
    return simpleName.replaceAll("Controller", this.fileNamSuffix);
  }

  public void setFileNamSuffix(String fileNamSuffix) {
    this.fileNamSuffix = fileNamSuffix;
  }
}
