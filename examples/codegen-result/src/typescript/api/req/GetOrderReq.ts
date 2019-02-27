    import {Order} from "../domain/Order";

/**
 * 合并方法参数生成的类
**/

export interface  GetOrderReq {

        /**
            *在java中的类型为：String[]
        **/
        names?: Array<string>;
        /**
            *在java中的类型为：List
            *在java中的类型为：Integer
        **/
        ids?: Array<number>;
        /**
            *在java中的类型为：Set
            *在java中的类型为：Order
        **/
        moneys?: Array<Order>;
}