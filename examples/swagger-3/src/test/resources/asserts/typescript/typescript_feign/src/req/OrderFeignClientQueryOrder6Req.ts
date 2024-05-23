/* tslint:disable */
/* eslint-disable */

        import {QueryOrderEvt} from "../evt/QueryOrderEvt";
import {DefaultOrderField} from "feign-client";

    /**
        * 合并方法参数生成的类
    **/


export interface  OrderFeignClientQueryOrder6Req {

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