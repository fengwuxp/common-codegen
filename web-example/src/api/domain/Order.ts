    import {User} from "@/src/api/domain/User";

/**
 * 订单
**/

export interface  Order {

        /**
            *添加时间
            *在java中的类型为：Date
        **/
        addTime: Date;
        /**
            *id
            *在java中的类型为：Long
        **/
        id?: number;
        /**
            *sn
            *在java中的类型为：String
        **/
        sn?: string;
        /**
            *下单用户
            *在java中的类型为：User
        **/
        user?: User;
}