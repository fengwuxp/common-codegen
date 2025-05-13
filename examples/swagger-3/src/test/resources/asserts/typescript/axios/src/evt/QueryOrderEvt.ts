/* tslint:disable */
/* eslint-disable */

        import {BaseQueryEvt} from "./BaseQueryEvt";
import {DefaultOrderField} from "feign-client";



export interface  QueryOrderEvt extends BaseQueryEvt {

            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：int
            **/
        ids?: number[];
}