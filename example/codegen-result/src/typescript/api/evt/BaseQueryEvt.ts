    import {BaseEvt} from "./BaseEvt";

/**
 * 统一的查询对象
**/

export interface  BaseQueryEvt extends BaseEvt {

        /**
            *属性说明：查询大小，示例输入：
            *在java中的类型为：Integer
        **/
        querySize?: number;
        /**
            *属性说明：查询页码，示例输入：
            *在java中的类型为：Integer
        **/
        queryPage?: number;
}