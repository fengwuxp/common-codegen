/* tslint:disable */
/* eslint-disable */

        import {Order} from "../domain/Order";
import {DefaultOrderField} from "feign-client";

    /**
        * 合并方法参数生成的类
    **/


export interface  OrderServiceGetOrderReq {

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