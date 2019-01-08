    import {BaseQueryEvt} from "@/src/api/evt/BaseQueryEvt";

/**
 **/

export interface  QueryOrderEvt extends BaseQueryEvt {

    /**
     *订单ns
     *输入字符串的最小长度为：0，输入字符串的最大长度为：50
     *在java中的类型为：String
     **/
    sn?: string;
}