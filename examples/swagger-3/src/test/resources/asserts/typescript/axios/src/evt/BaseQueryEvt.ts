/* tslint:disable */
/* eslint-disable */

        import {BaseEvt} from "./BaseEvt";



export interface  BaseQueryEvt extends BaseEvt {

            /**
                *字段在java中的类型为：Integer
            **/
        querySize?: number;
            /**
                *字段在java中的类型为：Integer
            **/
        queryPage?: number;
}