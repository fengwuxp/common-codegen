package com.wuxp.codegen.swagger3.evt;

import lombok.Data;

import javax.validation.constraints.NotNull;

      import com.wuxp.codegen.swagger3.enums.Sex;
      import java.util.Map;
      import com.wuxp.codegen.swagger3.evt.BaseEvt;



@Data
public class  CreateOrderEvt extends BaseEvt {

          /**
              *sn 约束条件：输入字符串的最小长度为：0，输入字符串的最大长度为：50
              *字段在java中的类型为：String
            **/
        private String sn;
          /**
              *字段在java中的类型为：Map
              *字段在java中的类型为：Sex
              *字段在java中的类型为：String
            **/
        private Map<Sex,String> test;
}
