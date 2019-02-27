import {PostMapping} from "common_fetch/src/annotations/mapping/PostMapping";
import {GetMapping} from "common_fetch/src/annotations/mapping/GetMapping";
import {DeleteMapping} from "common_fetch/src/annotations/mapping/DeleteMapping";
import {PutMapping} from "common_fetch/src/annotations/mapping/PutMapping";
import {PatchMapping} from "common_fetch/src/annotations/mapping/PatchMapping";
import {FetchOptions} from "common_fetch/src/FetchOptions";
import {Feign} from "common_fetch/src/annotations/Feign";
import {RequestMethod} from "common_fetch/src/constant/RequestMethod";
import {MediaType} from "common_fetch/src/constant/http/MediaType";

    import {CreateOrderEvt} from "../evt/CreateOrderEvt";
    import {GetOrderReq} from "../req/GetOrderReq";
    import {HelloReq} from "../req/HelloReq";
    import {QueryOrderEvt} from "../evt/QueryOrderEvt";
    import {QueryOrder2Req} from "../req/QueryOrder2Req";
    import {QueryPageReq} from "../req/QueryPageReq";
    import {Order} from "../domain/Order";
    import {ServiceResponse} from "../resp/ServiceResponse";
    import {PageInfo} from "../resp/PageInfo";
    import {User} from "../domain/User";
    import {ServiceQueryResponse} from "../resp/ServiceQueryResponse";

/**
    * 1:订单服务
    * 2:接口的请求方法为：POST
**/

    @Feign({
        value:'/order',
    })
 class OrderService{

    /**
        * 1:创建订单
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：ServiceResponse
        * 4:返回值在java中的类型为：Long
    **/
        @PostMapping({
        })
    createOrder:(req: CreateOrderEvt, option?: FetchOptions) => Promise<number>;
    /**
        * 1:获取订单列表
        * 2:接口的请求方法为：GET
        * 3:返回值在java中的类型为：List
        * 4:返回值在java中的类型为：Order
    **/
        @GetMapping({
            produces:[MediaType.FORM_DATA],
        })
    getOrder:(req: GetOrderReq, option?: FetchOptions) => Promise<Array<Order>>;
    /**
        * 1:test hello
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：ServiceResponse
    **/
        @PostMapping({
            produces:[MediaType.FORM_DATA],
        })
    hello:(req: HelloReq, option?: FetchOptions) => Promise<any>;
    /**
        * 1:获取订单列表
        * 2:接口的请求方法为：GET
        * 3:返回值在java中的类型为：PageInfo
        * 4:返回值在java中的类型为：Order
    **/
        @GetMapping({
        })
    queryOrder:(req: QueryOrderEvt, option?: FetchOptions) => Promise<PageInfo<Order>>;
    /**
        * 1:获取订单列表
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：ServiceQueryResponse
        * 4:返回值在java中的类型为：Order
    **/
        @PostMapping({
            produces:[MediaType.FORM_DATA],
        })
    queryOrder2:(req: QueryOrder2Req, option?: FetchOptions) => Promise<PageInfo<Order>>;
    /**
        * 1:查询分页
        * 2:接口的请求方法为：POST
        * 3:返回值在java中的类型为：ServiceResponse
        * 4:返回值在java中的类型为：PageInfo
        * 5:返回值在java中的类型为：Order
    **/
        @PostMapping({
            produces:[MediaType.FORM_DATA],
        })
    queryPage:(req: QueryPageReq, option?: FetchOptions) => Promise<PageInfo<Order>>;
}

export default new OrderService();