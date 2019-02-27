    import {User} from "./User";

/**
 * 订单
**/

export interface  Order {

        /**
            *属性说明：id，示例输入：
            *在java中的类型为：Long
        **/
        id?: number;
        /**
            *属性说明：sn，示例输入：order_sn_199223
            *在java中的类型为：String
        **/
        sn?: string;
        /**
            *属性说明：下单用户，示例输入：
            *在java中的类型为：User
        **/
        user?: User;
        /**
            *在java中的类型为：Date
        **/
        addTime?: Date;
}