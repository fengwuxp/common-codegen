package com.wuxp.codegen.core.util;

/**
 * 大小写转换
 *
 * @author wuxp
 */
public final class ToggleCaseUtils {

  private ToggleCaseUtils() {
  }

  /**
   * 将字符串的第一个字符大小写取反
   *
   * @param str
   * @return
   */
  public static String toggleFirstChart(String str) {
    if (str == null) {
      return null;
    }
    char[] chars = str.toCharArray();
    chars[0] = (char) (chars[0] ^ 0x20);
    return new String(chars);
  }


}
