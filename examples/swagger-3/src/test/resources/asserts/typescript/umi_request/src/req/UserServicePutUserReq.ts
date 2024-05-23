/* tslint:disable */
/* eslint-disable */

        import {Order} from "../domain/Order";
        import {User} from "../domain/User";
import {DefaultOrderField} from "feign-client";

    /**
        * 合并方法参数生成的类
    **/


export interface  UserServicePutUserReq {

            /**
                *字段在java中的类型为：Long
            **/
        id: string;
            /**
                *字段在java中的类型为：User
            **/
        user: User;
            /**
                *字段在java中的类型为：Order
            **/
        order: Order;
}