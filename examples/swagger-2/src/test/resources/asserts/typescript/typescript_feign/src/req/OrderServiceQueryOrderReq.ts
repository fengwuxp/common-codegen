/* tslint:disable */
/* eslint-disable */

        import {QueryOrderEvt} from "../evt/QueryOrderEvt";
import {DefaultOrderField} from "feign-client";

    /**
        * 合并方法参数生成的类
    **/


export interface  OrderServiceQueryOrderReq {

            /**
                *字段在java中的类型为：String
            **/
        userId: string;
            /**
                *字段在java中的类型为：QueryOrderEvt
            **/
        evt?: QueryOrderEvt;
}