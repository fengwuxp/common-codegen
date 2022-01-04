package com.wuxp.codegen.swagger2.enums;
  /**
   * 性别
   * 这是一个性别的枚举
   **/
public enum  Sex{

    MAN("男1",0,false),
    WOMAN("女1",1,true),
    NONE("未知1",2,false);

        private String desc;
        private String code;
        private String enabled;

Sex(String desc,Integer code,Boolean enabled) {
   this.desc = desc;
   this.code = code;
   this.enabled = enabled;
}

  public String getDesc() {
      return desc;
  }
  public String getCode() {
      return code;
  }
  public String getEnabled() {
      return enabled;
  }


}
