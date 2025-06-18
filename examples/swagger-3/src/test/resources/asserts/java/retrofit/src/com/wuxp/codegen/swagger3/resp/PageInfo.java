package com.wuxp.codegen.swagger3.resp;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import java.util.List;

@Data
@Accessors(chain = true)
public class  PageInfo<T> {

            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Object
            **/
        private List<T> records;

            /**
                *字段在java中的类型为：Integer
            **/
        private Integer queryPage;

            /**
                *字段在java中的类型为：Integer
            **/
        private Integer querySize;

}
