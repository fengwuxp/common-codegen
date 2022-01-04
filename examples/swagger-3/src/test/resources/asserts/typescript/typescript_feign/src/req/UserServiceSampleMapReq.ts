/* tslint:disable */
/* eslint-disable */

        import {Sex} from "../enums/Sex";

    /**
        * 合并方法参数生成的类
    **/


export interface  UserServiceSampleMapReq {

            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：Long
            **/
        ids?: string[];
            /**
                *字段在java中的类型为：String
            **/
        name?: string;
            /**
                *字段在java中的类型为：Sex
            **/
        sex?: 'MAN' | 'WOMAN' | 'NONE';
            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：Map
                *字段在java中的类型为：String
                *字段在java中的类型为：数组
                *字段在java中的类型为：String
            **/
        testParam?: Record<string,string[]>[];
}