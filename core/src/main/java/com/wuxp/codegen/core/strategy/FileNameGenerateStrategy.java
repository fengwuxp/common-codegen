package com.wuxp.codegen.core.strategy;

/**
 * 文件名称生成策略
 *
 * @author wxup
 */
@FunctionalInterface
public interface FileNameGenerateStrategy {

  FileNameGenerateStrategy DEFAULT = filepath -> filepath;

  /**
   * 生成文件名
   *
   * @param filepath
   * @return
   */
  String generateName(String filepath);

}
