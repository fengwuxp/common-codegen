package com.wuxp.codegen.model.enums;

/**
 * 访问权限控制
 */
public enum AccessPermission {


  PRIVATE("private"),

  PUBLIC("public"),

  PROTECTED("protected"),

  DEFAULT("");


  private String value;

  AccessPermission(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
