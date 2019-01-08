    import {BaseEvt} from "@/src/api/evt/BaseEvt";

/**
 * 统一的查询对象
**/

export interface  BaseQueryEvt extends BaseEvt {

        /**
            *查询大小
            *在java中的类型为：Integer
        **/
        querySize?: number;
        /**
            *查询页码
            *在java中的类型为：Integer
        **/
        queryPage?: number;
}