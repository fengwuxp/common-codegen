/* tslint:disable */
/* eslint-disable */

        import {User} from "./User";
        import {BaseInfo} from "./BaseInfo";



export interface  Order extends BaseInfo<string,string> {

            /**
                * ，默认值：，示例输入：
                *字段在java中的类型为：String
            **/
        sn?: string;
            /**
                *字段在java中的类型为：User
            **/
        user?: User;
            /**
                *字段在java中的类型为：Date
            **/
        addTime?: number;
}