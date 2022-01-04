package com.wuxp.codegen.swagger3.resp;

import lombok.Data;

import javax.validation.constraints.NotNull;




@Data
public class  ServiceResponse<T> {

          /**
              *字段在java中的类型为：String
          **/
         String message;
          /**
              *字段在java中的类型为：Integer
          **/
         Integer code;
         T data;
}
