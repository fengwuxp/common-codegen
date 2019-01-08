    import {Order} from "@/src/api/domain/Order";
    import {Sex} from "@/src/api/enums/Sex";

/**
 * 用户
**/

export interface  User {

        /**
            *是否为男孩
            *在java中的类型为：Boolean
        **/
        boy: boolean;
        /**
            *id
            *在java中的类型为：Long
        **/
        id?: number;
        /**
            *名称
            *在java中的类型为：String
        **/
        name?: string;
        /**
            *年龄
            *在java中的类型为：Integer
        **/
        age?: number;
        /**
            *订单列表
            *在java中的类型为：List
            *在java中的类型为：Order
        **/
        orderList?: Array<Order>;
        /**
            *性别
            *在java中的类型为：Sex
        **/
        sex?: Sex;
}