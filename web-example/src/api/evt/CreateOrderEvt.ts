    import {BaseEvt} from "@/src/api/evt/BaseEvt";

/**
 * 创建订单
**/

export interface  CreateOrderEvt extends BaseEvt {

        /**
            *订单ns
            *输入字符串的最小长度为：0，输入字符串的最大长度为：50
            *在java中的类型为：String
        **/
        sn?: string;
        /**
            *订单总价
            *必填项，不能为空
            *在java中的类型为：Integer
        **/
        totalAmount: number;
}