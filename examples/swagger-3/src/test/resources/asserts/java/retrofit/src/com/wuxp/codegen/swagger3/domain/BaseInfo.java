package com.wuxp.codegen.swagger3.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.*;
        import com.wuxp.codegen.swagger3.domain.BaseExample;

@Data
@Accessors(chain = true)
public class  BaseInfo<ID,T> {

        protected ID id;

        protected T data;

            /**
                *字段在java中的类型为：BaseExample
            **/
        protected BaseExample example;

}
