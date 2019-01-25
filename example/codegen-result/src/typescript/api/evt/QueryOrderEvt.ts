    import {BaseQueryEvt} from "./BaseQueryEvt";

/**
 * 测试的API接口方法一的请求参数
**/

export interface  QueryOrderEvt extends BaseQueryEvt {

        /**
            *属性说明：订单sn，示例输入：test method
            *属性：sn输入字符串的最小长度为：0，输入字符串的最大长度为：50
            *在java中的类型为：String
        **/
        sn?: string;
        /**
            *属性说明：id列表，示例输入：
            *在java中的类型为：int[]
        **/
        ids?: Array<number>;
}