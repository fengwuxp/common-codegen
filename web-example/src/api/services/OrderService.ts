import {RequestMapping} from "common_fetch/src/annotations/mapping/RequestMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";

    import {Order} from "@/src/api/domain/Order";
    import {ServiceResponse} from "@/src/api/resp/ServiceResponse";
    import {PageInfo} from "@/src/api/resp/PageInfo";
    import {User} from "@/src/api/domain/User";
    import {QueryOrderEvt} from "@/src/api/evt/QueryOrderEvt";
    import {CreateOrderEvt} from "@/src/api/evt/CreateOrderEvt";
    import {ServiceQueryResponse} from "@/src/api/resp/ServiceQueryResponse";

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
        * 3:返回值在java中的类型为：List
        * 4:返回值在java中的类型为：Order
    **/
        @GetMapping({
            value:'getOrder',
            method:RequestMethod.GET,
        })
    getOrder:(req: GetOrderReq, option?: FetchOptions) => Promise<Array<Order>>;
    /**
        * 1:创建订单
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：ServiceResponse
        * 4:返回值在java中的类型为：Long
    **/
        @PostMapping({
            value:'createOrder',
            method:RequestMethod.POST,
        })
    createOrder:(req: CreateOrderEvt, option?: FetchOptions) => Promise<number>;
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
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：ServiceQueryResponse
        * 4:返回值在java中的类型为：Order
    **/
        @PostMapping({
            value:'queryOrder2',
            method:RequestMethod.POST,
        })
    queryOrder2:(req: QueryOrder2Req, option?: FetchOptions) => Promise<PageInfo<Order>>;
}