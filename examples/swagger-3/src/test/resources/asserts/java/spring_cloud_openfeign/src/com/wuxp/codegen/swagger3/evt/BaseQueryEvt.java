package com.wuxp.codegen.swagger3.evt;

import lombok.Data;

import javax.validation.constraints.NotNull;

      import com.wuxp.codegen.swagger3.evt.BaseEvt;



@Data
public class  BaseQueryEvt extends BaseEvt {

          /**
              *字段在java中的类型为：Integer
            **/
         Integer querySize;
          /**
              *字段在java中的类型为：Integer
            **/
         Integer queryPage;
}
