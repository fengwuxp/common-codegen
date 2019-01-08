    import {User} from "@/src/api/domain/User";

/**
 **/

export interface  Order {

    /**
     **/
    addTime: Date;
    /**
     *id
     *在java中的类型为：Long
     **/
    id?: number;
    /**
     *sn
     *在java中的类型为：String
     **/
    sn?: string;
    /**
     *下单用户
     *在java中的类型为：User
     **/
    user?: User;
}