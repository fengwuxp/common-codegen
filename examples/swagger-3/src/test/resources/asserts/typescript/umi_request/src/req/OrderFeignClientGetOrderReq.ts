/* tslint:disable */
/* eslint-disable */

        import {Order} from "../domain/Order";
        import {User} from "../domain/User";

    /**
        * 合并方法参数生成的类
    **/


export interface  OrderFeignClientGetOrderReq {

            /**
                * ，默认值：，示例输入：
                *字段在java中的类型为：String
            **/
        sn?: string;
            /**
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
        names: string[];
            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Integer
            **/
        ids: Array<number>;
            /**
                *字段在java中的类型为：Set
                *字段在java中的类型为：Order
            **/
        moneys?: Array<Order>;
}