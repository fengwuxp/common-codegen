package com.wuxp.codegen.swagger3.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import com.wuxp.codegen.swagger3.domain.BaseExample;

@Data
@Accessors(chain = true)
public class  BaseInfo<I,T> {

        protected I id;

        protected T data;

            /**
                *字段在java中的类型为：BaseExample
            **/
        protected BaseExample example;

}
