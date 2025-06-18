package com.wuxp.codegen.swagger3;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Accessors(chain = true)
public class  ExampleDto {

            /**
                *字段在java中的类型为：Integer
            **/
         Integer querySize;

            /**
                *字段在java中的类型为：Integer
            **/
         Integer queryPage;

}
