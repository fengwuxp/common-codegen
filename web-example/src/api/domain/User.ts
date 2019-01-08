    import {Order} from "@/src/api/domain/Order";
    import {Sex} from "@/src/api/enums/Sex";

/**
**/

export interface  User{

    /**
    **/
    boy: boolean;
    /**
        * 1:在java中的类型为：Long
    **/
    id?: number;
    /**
        * 1:在java中的类型为：String
    **/
    name?: string;
    /**
        * 1:在java中的类型为：Integer
    **/
    age?: number;
    /**
        * 1:在java中的类型为：List
        * 2:在java中的类型为：Order
    **/
    orderList?: Array<Order>;
    /**
        * 1:在java中的类型为：Sex
    **/
    sex?: Sex;
}