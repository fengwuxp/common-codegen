    import {BaseQueryEvt} from "@api/evt/BaseQueryEvt";

/**
 * 测试的API接口方法一的请求参数
**/

export interface  QueryOrderEvt extends BaseQueryEvt {

        /**
            *订单ns
            *输入字符串的最小长度为：0，输入字符串的最大长度为：50
            *在java中的类型为：String
        **/
        sn?: string;
}