package com.wuxp.codegen.swagger3.resp;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import com.wuxp.codegen.swagger3.resp.TestMethodDTO;

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

            /**
                *字段在java中的类型为：String
            **/
        private String name;

            /**
                *字段在java中的类型为：String
            **/
        private String keyword;

            /**
                *字段在java中的类型为：TestMethodDTO
            **/
        private TestMethodDTO method;

}
