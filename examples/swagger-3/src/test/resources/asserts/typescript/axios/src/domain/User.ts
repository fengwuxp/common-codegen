/* tslint:disable */
/* eslint-disable */

        import {Order} from "./Order";
        import {Sex} from "../enums/Sex";

    /**
        *  用户信息 用户信息描述，默认值：，示例输入：
    **/


export interface  User {

            /**
                * id 用户ID，默认值：，示例输入：
                *字段在java中的类型为：Long
            **/
        id?: string;
            /**
                * 姓名 用户名称，默认值：，示例输入：
                *字段在java中的类型为：String
            **/
        name?: string;
            /**
                *字段在java中的类型为：Integer
            **/
        age?: number;
            /**
                *字段在java中的类型为：Order
            **/
        order?: Order;
            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Order
            **/
        orderList?: Array<Order>;
            /**
                *字段在java中的类型为：Sex
            **/
        sex?: 'MAN' | 'WOMAN' | 'NONE';
            /**
                *字段在java中的类型为：Map
                *字段在java中的类型为：String
                *字段在java中的类型为：String
            **/
        other?: Record<string,string>;
            /**
                *字段在java中的类型为：Map
                *字段在java中的类型为：Object
                *字段在java中的类型为：Object
            **/
        other2?: Record<any,any>;
            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Object
            **/
        list?: Array<any>;
            /**
                *字段在java中的类型为：List
                *字段在java中的类型为：Object
            **/
        list2?: Array<any>;
            /**
                *字段在java中的类型为：String
            **/
        myFriends?: string;
            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：String
            **/
        demos?: string[][];
            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：String
            **/
        demos2?: string[][][];
            /**
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：Map
                *字段在java中的类型为：数组
                *字段在java中的类型为：Sex
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：数组
                *字段在java中的类型为：String
            **/
        demos3?: Record<'MAN' | 'WOMAN' | 'NONE'[],string[][][]>[][][];
            /**
                *字段在java中的类型为：Boolean
            **/
        boy?: boolean;
}