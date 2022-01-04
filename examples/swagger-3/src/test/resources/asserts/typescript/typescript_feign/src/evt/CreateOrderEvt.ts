/* tslint:disable */
/* eslint-disable */

        import {Sex} from "../enums/Sex";
        import {BaseEvt} from "./BaseEvt";



export interface  CreateOrderEvt extends BaseEvt {

            /**
                *sn 约束条件：输入字符串的最小长度为：0，输入字符串的最大长度为：50
                *字段在java中的类型为：String
            **/
        sn?: string;
            /**
                *字段在java中的类型为：Map
                *字段在java中的类型为：Sex
                *字段在java中的类型为：String
            **/
        test?: Record<'MAN' | 'WOMAN' | 'NONE',string>;
}