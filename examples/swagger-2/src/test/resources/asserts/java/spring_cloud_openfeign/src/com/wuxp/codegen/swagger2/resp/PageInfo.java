package com.wuxp.codegen.swagger2.resp;

import lombok.Data;
import lombok.experimental.Accessors;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import java.util.List;

    /**
        * 分页对象
    **/
@Data
@Accessors(chain = true)
public class  PageInfo<T> {

            /**
                *属性说明：响应集合列表，示例输入：
                *字段在java中的类型为：List
                *字段在java中的类型为：Object
            **/
         List<T> records;

            /**
                *属性说明：查询页码，示例输入：
                *字段在java中的类型为：Integer
            **/
         Integer queryPage;

            /**
                *属性说明：查询大小，示例输入：
                *字段在java中的类型为：Integer
            **/
         Integer querySize;

}
