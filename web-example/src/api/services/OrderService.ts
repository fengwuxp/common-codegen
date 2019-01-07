import {RequestMapping} from "common_fetch/src/annotations/mapping/RequestMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";

    import {Order} from "@/src/api/domain/Order";
    import {PageInfo} from "@/src/api/resp/PageInfo";
    import {User} from "@/src/api/domain/User";
    import {QueryOrderEvt} from "@/src/api/evt/QueryOrderEvt";

/**
    * 1:订单服务
    * 2:接口的请求方法为：POST
**/

    @Feign({
        value:'/order',
    })
export default class OrderService{

    /**
        * 1:接口的请求方法为：GET
        * 2:返回值在java中的类型为：List
        * 3:返回值在java中的类型为：Order
    **/
        @GetMapping({
            value:'getOrder',
            method:RequestMethod.GET,
        })
    getOrder:(req: GetOrderReq, option?: FetchOptions) => Promise<Array>;
    /**
        * 1:接口的请求方法为：GET
        * 2:返回值在java中的类型为：PageInfo
        * 3:返回值在java中的类型为：Order
    **/
        @RequestMapping({
            value:'getOrder',
            method:RequestMethod.GET,
        })
    queryOrder:(req: QueryOrderEvt, option?: FetchOptions) => Promise<PageInfo>;
}