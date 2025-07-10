package com.wuxp.codegen.swagger3.resp;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import java.util.Date;

@Data
@Accessors(chain = true)
public class  TestMethodDTO {

            /**
                *字段在java中的类型为：String
            **/
        private String name;

            /**
                *字段在java中的类型为：Short
            **/
        private Short age;

            /**
                *字段在java中的类型为：Boolean
            **/
        private Boolean flag;

            /**
                *字段在java中的类型为：Date
            **/
        private Date birthDay;

            /**
                *字段在java中的类型为：TestMethodDTO
            **/
        private TestMethodDTO example;

}
