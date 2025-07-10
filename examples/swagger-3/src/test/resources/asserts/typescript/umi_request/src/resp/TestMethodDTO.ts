/* tslint:disable */
/* eslint-disable */

import {DefaultOrderField} from "feign-client";



export interface  TestMethodDTO {

            /**
                *字段在java中的类型为：String
            **/
        name?: string;
            /**
                *字段在java中的类型为：Short
            **/
        age?: number;
            /**
                *字段在java中的类型为：Boolean
            **/
        flag?: boolean;
            /**
                *字段在java中的类型为：Date
            **/
        birthDay?: number;
            /**
                *字段在java中的类型为：TestMethodDTO
            **/
        example?: TestMethodDTO;
}