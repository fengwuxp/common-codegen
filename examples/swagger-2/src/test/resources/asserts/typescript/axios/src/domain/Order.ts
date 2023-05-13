/* tslint:disable */
/* eslint-disable */

        import {User} from "./User";
        import {BaseInfo} from "./BaseInfo";

    /**
        * 订单
    **/


export interface  Order extends BaseInfo<string> {

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
}