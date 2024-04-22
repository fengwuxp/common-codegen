/* tslint:disable */
/* eslint-disable */

        import {Order} from "../domain/Order";
        import {User} from "../domain/User";
        import {Sex} from "../enums/Sex";
        import {ExampleEnum} from "../enums/ExampleEnum";
import {DefaultOrderField} from "feign-client";

    /**
        * 合并方法参数生成的类
    **/


export interface  UserServicePostUserReq {

            /**
                *属性说明：id，示例输入：
                *字段在java中的类型为：Long
            **/
        id?: string;
            /**
                *属性说明：名称，示例输入：
                *字段在java中的类型为：String
            **/
        name?: string;
            /**
                *属性说明：年龄，示例输入：
                *字段在java中的类型为：Integer
            **/
        age?: number;
            /**
                *属性说明：订单列表，示例输入：
                *字段在java中的类型为：List
                *字段在java中的类型为：Order
            **/
        orderList?: Array<Order>;
            /**
                *属性说明：性别，示例输入：
                *字段在java中的类型为：Sex
            **/
        sex?: 'MAN' | 'WOMAN' | 'NONE';
            /**
                *属性说明：其他，示例输入：
                *字段在java中的类型为：Map
                *字段在java中的类型为：String
                *字段在java中的类型为：String
            **/
        other?: Record<string,string>;
            /**
                *属性说明：其他2，示例输入：
                *字段在java中的类型为：Map
                *字段在java中的类型为：Object
                *字段在java中的类型为：Object
            **/
        other2?: Record<any,any>;
            /**
                *属性说明：list，示例输入：
                *字段在java中的类型为：List
                *字段在java中的类型为：Object
            **/
        list?: Array<any>;
            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Object
            **/
        list2?: Array<any>;
            /**
                *属性说明：myFriends，示例输入：
                *字段在java中的类型为：String
            **/
        myFriends?: string;
            /**
                *属性说明：example enum，示例输入：
                *字段在java中的类型为：ExampleEnum
            **/
        exampleEnum?: 'MAN' | 'WOMAN' | 'NONE';
            /**
                *字段在java中的类型为：Boolean
            **/
        boy?: boolean;
            /**
                *属性说明：sn，示例输入：order_sn_199223
                *字段在java中的类型为：String
            **/
        sn?: string;
            /**
                *属性说明：下单用户，示例输入：
                *字段在java中的类型为：User
            **/
        user?: User;
            /**
                *字段在java中的类型为：Date
            **/
        addTime?: number;
            /**
                *字段在java中的类型为：Order
            **/
        order?: Order;
}