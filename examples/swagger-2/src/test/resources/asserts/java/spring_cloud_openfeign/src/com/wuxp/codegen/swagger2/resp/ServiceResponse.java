package com.wuxp.codegen.swagger2.resp;

import lombok.Data;
import javax.validation.constraints.*;

    /**
        * 服务响应对象
    **/
@Data
public class  ServiceResponse<T> {

            /**
                *属性说明：响应数据消息，示例输入：
                *字段在java中的类型为：String
            **/
         String message;

            /**
                *属性说明：响应数据code，示例输入：
                *字段在java中的类型为：Integer
            **/
         Integer code;

            /**
                *属性说明：响应数据，示例输入：
            **/
         T data;

}
