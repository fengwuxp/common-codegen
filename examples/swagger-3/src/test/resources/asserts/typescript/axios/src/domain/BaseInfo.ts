/* tslint:disable */
/* eslint-disable */

        import {BaseExample} from "./BaseExample";
import {DefaultOrderField} from "feign-client";



export interface  BaseInfo<I,T> {

        id?: I;
        data?: T;
            /**
                *字段在java中的类型为：BaseExample
            **/
        example?: 'A';
}