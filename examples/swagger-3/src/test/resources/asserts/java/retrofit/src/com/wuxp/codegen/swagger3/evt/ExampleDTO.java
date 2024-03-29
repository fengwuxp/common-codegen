package com.wuxp.codegen.swagger3.evt;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
public class  ExampleDTO {

            /**
                *字段在java中的类型为：Integer
            **/
         Integer querySize;

            /**
                *字段在java中的类型为：Integer
            **/
         Integer queryPage;

}
