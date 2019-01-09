import {RequestMapping} from "common_fetch/src/annotations/mapping/RequestMapping";
import {PostMapping} from "common_fetch/src/annotations/mapping/PostMapping";
import {GetMapping} from "common_fetch/src/annotations/mapping/GetMapping";
import {DeleteMapping} from "common_fetch/src/annotations/mapping/DeleteMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";

    import {Order} from "@api/api/domain/Order";
    import {PageInfo} from "@api/api/resp/PageInfo";
    import {ServiceResponse} from "@api/api/resp/ServiceResponse";
    import {User} from "@api/api/domain/User";
    import {CreateOrderEvt} from "@api/api/evt/CreateOrderEvt";
    import {QueryOrderEvt} from "@api/api/evt/QueryOrderEvt";
    import {ServiceQueryResponse} from "@api/api/resp/ServiceQueryResponse";

/**
    * 1:订单服务
    * 2:接口的请求方法为：POST
**/

    @Feign({
        value:'/order',
    })
export default class OrderService{

    /**
        * 1:获取订单列表
        * 2:接口的请求方法为：GET
        * 3:返回值在java中的类型为：PageInfo
        * 4:返回值在java中的类型为：Order
    **/
        @RequestMapping({
            method:RequestMethod.GET,
        })
    queryOrder:(req: QueryOrderEvt, option?: FetchOptions) => Promise<PageInfo<Order>>;
    /**
        * 1:获取订单列表
        * 2:接口的请求方法为：GET
        * 3:返回值在java中的类型为：List
        * 4:返回值在java中的类型为：Order
    **/
        @GetMapping({
            value:'getOrder',
        })
    getOrder:(req: GetOrderReq, option?: FetchOptions) => Promise<Array<Order>>;
    /**
        * 1:获取订单列表
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：ServiceQueryResponse
        * 4:返回值在java中的类型为：Order
    **/
        @PostMapping({
            value:'queryOrder2',
        })
    queryOrder2:(req: QueryOrder2Req, option?: FetchOptions) => Promise<PageInfo<Order>>;
    /**
        * 1:创建订单
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：ServiceResponse
        * 4:返回值在java中的类型为：Long
    **/
        @PostMapping({
            value:'createOrder',
        })
    createOrder:(req: CreateOrderEvt, option?: FetchOptions) => Promise<number>;
}