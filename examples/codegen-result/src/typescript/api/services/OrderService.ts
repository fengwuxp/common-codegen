import {RequestMapping} from "common_fetch/src/annotations/mapping/RequestMapping";
import {PostMapping} from "common_fetch/src/annotations/mapping/PostMapping";
import {GetMapping} from "common_fetch/src/annotations/mapping/GetMapping";
import {DeleteMapping} from "common_fetch/src/annotations/mapping/DeleteMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";
import {MediaType} from "common_fetch/src/constant/http/MediaType";

    import {QueryPageReq} from "../req/QueryPageReq";
    import {GetOrderReq} from "../req/GetOrderReq";
    import {HelloReq} from "../req/HelloReq";
    import {QueryOrder2Req} from "../req/QueryOrder2Req";
    import {Order} from "../domain/Order";
    import {ServiceResponse} from "../resp/ServiceResponse";
    import {PageInfo} from "../resp/PageInfo";
    import {User} from "../domain/User";
    import {CreateOrderEvt} from "../evt/CreateOrderEvt";
    import {QueryOrderEvt} from "../evt/QueryOrderEvt";
    import {ServiceQueryResponse} from "../resp/ServiceQueryResponse";

/**
    * 1:接口的请求方法为：POST
**/

    @RequestMapping({
        value:'/order',
        method:RequestMethod.POST,
    })
 class OrderService{

    /**
        * 1:接口的请求方法为：POST
        * 2:返回值在java中的类型为：ServiceResponse
        * 3:返回值在java中的类型为：PageInfo
        * 4:返回值在java中的类型为：Order
    **/
        @PostMapping({
            produces:[MediaType.JSON],
            consumes:[MediaType.JSON],
        })
    queryPage:(req: QueryPageReq, option?: FetchOptions) => Promise<PageInfo<Order>>;
    /**
        * 1:接口的请求方法为：GET
        * 2:返回值在java中的类型为：List
        * 3:返回值在java中的类型为：Order
    **/
        @GetMapping({
        })
    getOrder:(req: GetOrderReq, option?: FetchOptions) => Promise<Array<Order>>;
    /**
        * 1:接口的请求方法为：GET
        * 2:返回值在java中的类型为：PageInfo
        * 3:返回值在java中的类型为：Order
    **/
        @RequestMapping({
            method:RequestMethod.GET,
        })
    queryOrder:(req: QueryOrderEvt, option?: FetchOptions) => Promise<PageInfo<Order>>;
    /**
        * 1:接口的请求方法为：POST
        * 2:返回值在java中的类型为：ServiceResponse
        * 3:返回值在java中的类型为：Long
    **/
        @PostMapping({
        })
    createOrder:(req: CreateOrderEvt, option?: FetchOptions) => Promise<number>;
    /**
        * 1:接口的请求方法为：POST
        * 2:返回值在java中的类型为：ServiceResponse
    **/
        @PostMapping({
        })
    hello:(req: HelloReq, option?: FetchOptions) => Promise<any>;
    /**
        * 1:接口的请求方法为：POST
        * 2:返回值在java中的类型为：ServiceQueryResponse
        * 3:返回值在java中的类型为：Order
    **/
        @PostMapping({
            produces:[MediaType.FORM_DATA],
            consumes:[MediaType.JSON],
        })
    queryOrder2:(req: QueryOrder2Req, option?: FetchOptions) => Promise<PageInfo<Order>>;
}

export default new OrderService();