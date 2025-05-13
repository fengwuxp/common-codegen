/* tslint:disable */
/* eslint-disable */

        import {BaseExample} from "./BaseExample";
import {DefaultOrderField} from "feign-client";



export interface  BaseInfo<ID,T> {

        id?: ID;
        data?: T;
            /**
                *字段在java中的类型为：BaseExample
            **/
        example?: 'A';
}