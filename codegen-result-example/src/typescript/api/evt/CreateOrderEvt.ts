    import {BaseEvt} from "./BaseEvt";

/**
 * 创建订单
**/

export interface  CreateOrderEvt extends BaseEvt {

        /**
            *属性说明：订单ns，示例输入：test method
            *属性：sn输入字符串的最小长度为：0，输入字符串的最大长度为：50
            *在java中的类型为：String
        **/
        sn?: string;
        /**
            *属性说明：订单总价，示例输入：
            *属性：totalAmount为必填项，不能为空
            *在java中的类型为：Integer
        **/
        totalAmount: number;
}