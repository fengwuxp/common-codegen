package com.wuxp.codegen.swagger2.evt;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import com.wuxp.codegen.swagger2.evt.BaseEvt;

    /**
        * 统一的查询对象
    **/
@Data
@Accessors(chain = true)
public class  BaseQueryEvt extends BaseEvt {

            /**
                *属性说明：查询大小，示例输入：
                *字段在java中的类型为：Integer
            **/
         Integer querySize;

            /**
                *属性说明：查询页码，示例输入：
                *字段在java中的类型为：Integer
            **/
         Integer queryPage;

}
