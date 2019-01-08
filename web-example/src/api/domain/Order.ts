    import {User} from "@/src/api/domain/User";

/**
**/

export interface  Order{

    /**
    **/
    addTime: Date;
    /**
        * 1:在java中的类型为：Long
    **/
    id?: number;
    /**
        * 1:在java中的类型为：String
    **/
    sn?: string;
    /**
        * 1:在java中的类型为：User
    **/
    user?: User;
}