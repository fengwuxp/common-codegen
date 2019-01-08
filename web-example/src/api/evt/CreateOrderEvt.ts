    import {BaseEvt} from "@/src/api/evt/BaseEvt";

/**
**/

export interface  CreateOrderEvt{

    /**
        * 1:输入字符串的最小长度为：0，输入字符串的最大长度为：50
        * 2:在java中的类型为：String
    **/
    sn?: string;
    /**
        * 1:必填项，不能为空
        * 2:在java中的类型为：Integer
    **/
    totalAmount: number;
}