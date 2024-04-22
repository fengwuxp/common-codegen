package com.wuxp.codegen.swagger2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
  /**
   * 性别
   **/
@AllArgsConstructor
@Getter
public enum  Sex{

    MAN("男1",0,false),
    WOMAN("女1",1,true),
    NONE("未知1",2,false);

        private final String desc;
        private final Integer code;
        private final Boolean enabled;


}
