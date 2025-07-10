/* tslint:disable */
/* eslint-disable */

        import {TestMethodDTO} from "./TestMethodDTO";
import {DefaultOrderField} from "feign-client";



export interface  ExampleDTO {

            /**
                *字段在java中的类型为：Integer
            **/
        querySize?: number;
            /**
                *字段在java中的类型为：Integer
            **/
        queryPage?: number;
            /**
                *字段在java中的类型为：String
            **/
        name?: string;
            /**
                *字段在java中的类型为：String
            **/
        keyword?: string;
            /**
                *字段在java中的类型为：TestMethodDTO
            **/
        method?: TestMethodDTO;
}