package com.wuxp.codegen.swagger2.domain;

import lombok.Data;
import javax.validation.constraints.*;
        import com.wuxp.codegen.swagger2.domain.BaseExample;

@Data
public class  BaseInfo<ID> {

            /**
                *属性说明：id 我的 \n 你的  他的 \r 不是的，示例输入：
            **/
        protected ID id;

            /**
                *字段在java中的类型为：BaseExample
            **/
        protected BaseExample example;

}
