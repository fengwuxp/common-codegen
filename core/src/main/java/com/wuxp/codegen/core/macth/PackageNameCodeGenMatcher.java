package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 根据包名进行匹配，支持引入和排除的2中模式
 *
 * @author wxup
 */
@Slf4j
public class PackageNameCodeGenMatcher implements CodeGenMatcher {

  /**
   * 需要全局忽略的包名列表
   */
  private static final List<String> IGNORE_PACKAGE_LIST = new ArrayList<>();

  /**
   * 需要包全局含的包
   */
  private static final List<String> INCLUDE_PACKAGE_LIST = new ArrayList<>();


  static {
    IGNORE_PACKAGE_LIST.add("org.springframework.");
    IGNORE_PACKAGE_LIST.add("org.slf4j.");
    IGNORE_PACKAGE_LIST.add("org.apache.");
    IGNORE_PACKAGE_LIST.add("org.freemarker.");
    IGNORE_PACKAGE_LIST.add("org.hibernate.");
    IGNORE_PACKAGE_LIST.add("org.jetbrains.");
    IGNORE_PACKAGE_LIST.add("org.jodd.");
    IGNORE_PACKAGE_LIST.add("org.apache.commons.");
    IGNORE_PACKAGE_LIST.add("lombok.");
    IGNORE_PACKAGE_LIST.add("javax.persistence.");
    IGNORE_PACKAGE_LIST.add("javax.servlet.");
    IGNORE_PACKAGE_LIST.add("java.security.");
    IGNORE_PACKAGE_LIST.add("java.text.");
    IGNORE_PACKAGE_LIST.add("java.io.");
    IGNORE_PACKAGE_LIST.add("java.time.");
    IGNORE_PACKAGE_LIST.add("java.util.concurrent.");
    IGNORE_PACKAGE_LIST.add("sun.");
    IGNORE_PACKAGE_LIST.add("com.google.");
    IGNORE_PACKAGE_LIST.add("com.alibaba.");
    IGNORE_PACKAGE_LIST.add("com.alipay.");
    IGNORE_PACKAGE_LIST.add("com.baidu.");
    IGNORE_PACKAGE_LIST.add("com.github.");
    IGNORE_PACKAGE_LIST.add("com.wuxp.basic.");

    /**
     * 文件上传
     */
    INCLUDE_PACKAGE_LIST.add("org.springframework.web.multipart.commons.CommonsMultipartFile");
  }

  private final List<String> ignorePackages;

  private final List<String> includePackages;

  public PackageNameCodeGenMatcher() {
    this(new ArrayList<>(), new ArrayList<>());
  }

  public PackageNameCodeGenMatcher(List<String> ignorePackages, List<String> includePackages) {
    this.ignorePackages = ignorePackages;
    this.includePackages = includePackages;
    this.ignorePackages.addAll(IGNORE_PACKAGE_LIST);
    this.includePackages.addAll(INCLUDE_PACKAGE_LIST);
  }

  @Override
  public boolean match(Class<?> clazz) {
    if (clazz == null) {
      return false;
    }

    //在包含列表里面
    boolean anyMatch = includePackages.stream().anyMatch(name -> clazz.getName().startsWith(name));
    if (anyMatch) {
      return true;
    }

    // 不在忽略列表里面则返回true
    return ignorePackages.stream().noneMatch(name -> clazz.getName().startsWith(name));
  }

  public void addIgnorePackages(Collection<String> ignorePackages) {
    this.ignorePackages.addAll(ignorePackages);
  }

  public void addIncludePackages(Collection<String> includePackages) {
    this.includePackages.addAll(includePackages);
  }

}
