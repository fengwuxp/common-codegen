/* tslint:disable */
/* eslint-disable */

        import {Order} from "../domain/Order";
        import {User} from "../domain/User";

    /**
        * 合并方法参数生成的类
    **/


export interface  OrderServiceGetOrderReq {

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
                *字段在java中的类型为：数组
                *字段在java中的类型为：String
            **/
        names?: string[];
            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Integer
            **/
        ids?: Array<number>;
            /**
                *字段在java中的类型为：Set
                *字段在java中的类型为：Order
            **/
        moneys?: Array<Order>;
}