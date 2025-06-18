package com.wuxp.codegen.swagger3.resp;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Accessors(chain = true)
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
