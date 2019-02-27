    import {Order} from "./Order";
    import {Sex} from "../enums/Sex";

/**
 * 用户
**/

export interface  PutUserReq {

        /**
            *在java中的类型为：Long
            *用户ID
        **/
        id: number;
        /**
            *属性说明：年龄，示例输入：
            *在java中的类型为：Integer
        **/
        age?: number;
        /**
            *属性说明：list，示例输入：
            *在java中的类型为：List
        **/
        list?: Array<any>;
        /**
            *在java中的类型为：List
        **/
        list2?: Array<any>;
        /**
            *属性说明：名称，示例输入：
            *在java中的类型为：String
            *用户详细实体user
        **/
        name: string;
        /**
            *属性说明：订单列表，示例输入：
            *在java中的类型为：List
            *在java中的类型为：Order
        **/
        orderList?: Array<Order>;
        /**
            *属性说明：其他，示例输入：
            *在java中的类型为：Map
            *在java中的类型为：String
            *在java中的类型为：String
        **/
        other?: Map<string,string>;
        /**
            *属性说明：其他2，示例输入：
            *在java中的类型为：Map
        **/
        other2?: Map<any,any>;
        /**
            *属性说明：性别，示例输入：
            *在java中的类型为：Sex
        **/
        sex?: Sex;
        /**
            *在java中的类型为：Boolean
        **/
        boy?: boolean;
}