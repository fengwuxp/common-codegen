package com.wuxp.codegen.swagger3.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import com.wuxp.codegen.swagger3.domain.Order;
        import com.wuxp.codegen.swagger3.enums.Sex;
        import java.util.List;
        import java.util.Map;

    /**
        *  用户信息 用户信息描述，默认值：，示例输入：
    **/
@Data
@Accessors(chain = true)
public class  User {

            /**
                * id 用户ID，默认值：，示例输入：
                *字段在java中的类型为：Long
            **/
        private Long id;

            /**
                * 姓名 用户名称，默认值：，示例输入：
                *字段在java中的类型为：String
            **/
        private String name;

            /**
                *字段在java中的类型为：Integer
            **/
        private Integer age;

            /**
                *字段在java中的类型为：Order
            **/
        private Order order;

            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Order
            **/
        private List<Order> orderList;

            /**
                *字段在java中的类型为：Sex
            **/
        private Sex sex;

            /**
                *字段在java中的类型为：Map
                *字段在java中的类型为：String
                *字段在java中的类型为：String
            **/
        private Map<String,String> other;

            /**
                *字段在java中的类型为：Map
                *字段在java中的类型为：Object
                *字段在java中的类型为：Object
            **/
        private Map<Object,Object> other2;

            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Object
            **/
        private List<Object> list;

            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Object
            **/
        private List<Object> list2;

            /**
                *字段在java中的类型为：String
            **/
        private String myFriends;

            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：String
            **/
        private String[][] demos;

            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：String
            **/
        private String[][][] demos2;

            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：Map
                *字段在java中的类型为：数组
                *字段在java中的类型为：Sex
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：String
            **/
        private Map<Sex[],String[][][]>[][][] demos3;

            /**
                *字段在java中的类型为：Boolean
            **/
        private Boolean boy;

}
