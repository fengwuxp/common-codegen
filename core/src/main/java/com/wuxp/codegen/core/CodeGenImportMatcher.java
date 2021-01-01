package com.wuxp.codegen.core;

/**
 * 用于匹配那些只需要导入而不需要导入的类型
 * <p>
 * 对应一些通用的模型对象，我们可能会提取到一个公共的库中，所以这些对象，我们不需要而外生成
 * </p>
 *
 * @author wuxp
 */
public interface CodeGenImportMatcher extends CodeGenMatcher {
}
