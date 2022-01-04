package com.wuxp.codegen.swagger3.resp;

import lombok.Data;

import javax.validation.constraints.NotNull;

      import java.util.List;



@Data
public class  PageInfo<T> {

          /**
              *字段在java中的类型为：List
              *字段在java中的类型为：Object
          **/
         List<T> records;
          /**
              *字段在java中的类型为：Integer
          **/
         Integer queryPage;
          /**
              *字段在java中的类型为：Integer
          **/
         Integer querySize;
}
