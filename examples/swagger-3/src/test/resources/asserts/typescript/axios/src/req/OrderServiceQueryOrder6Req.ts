/* tslint:disable */
/* eslint-disable */

        import {QueryOrderEvt} from "../evt/QueryOrderEvt";

    /**
        * 合并方法参数生成的类
    **/


export interface  OrderServiceQueryOrder6Req {

            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：int
            **/
        ids?: number[];
            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：QueryOrderEvt
            **/
        evt: Array<QueryOrderEvt>;
            /**
                *字段在java中的类型为：Long
            **/
        memberId?: string;
}