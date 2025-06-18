/* tslint:disable */
/* eslint-disable */

import {
Feign,
RequestMapping,
PostMapping,
DeleteMapping,
GetMapping,
PutMapping,
FeignHttpClientPromiseFunction,
feignHttpFunctionBuilder,
FeignRequestOptions} from "feign-client";
import {HttpMediaType} from "wind-common-utils/lib/http/HttpMediaType";
      import {Order} from "../../domain/Order";
      import {QueryOrderEvt} from "../../evt/QueryOrderEvt";
      import {CreateOrderEvt} from "../../evt/CreateOrderEvt";
      import {OrderFeignClientQueryPageReq} from "../../req/OrderFeignClientQueryPageReq";
      import {OrderFeignClientHelloReq} from "../../req/OrderFeignClientHelloReq";
      import {ExampleDTO} from "../../evt/ExampleDTO";
      import {OrderFeignClientQueryOrder_2Req} from "../../req/OrderFeignClientQueryOrder_2Req";
      import {PageInfo} from "../../resp/PageInfo";
      import {OrderFeignClientQueryOrder6Req} from "../../req/OrderFeignClientQueryOrder6Req";
      import {OrderFeignClientGetOrderReq} from "../../req/OrderFeignClientGetOrderReq";

    /**
     * 接口：GET
    **/
  @Feign({
        value:"/order",
  })
class OrderFeignClient{

    /**
      * 1:GET /order/getOrder
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Order
     **/
      @GetMapping({
            headers:{"My-Ids":"{ids}"},
            queryArgNames:["names"],
      })
    getOrder!:(req: OrderFeignClientGetOrderReq, option?: FeignRequestOptions) => Promise<Array<Order>>;
    /**
      * 1:GET /order
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @GetMapping({
      })
    queryOrder2!:(req?: QueryOrderEvt, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:POST /order
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder!:(req?: QueryOrderEvt, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:POST /order/queryOrder3
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder3!:(req: QueryOrderEvt[], option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:POST /order/queryOrder4
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder4!:(req: Array<QueryOrderEvt>, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:POST /order/queryOrder5
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder5!:(req: Record<string,QueryOrderEvt>, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:POST /order/queryOrder6
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder6!:(req: OrderFeignClientQueryOrder6Req, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:POST /order/queryOrder_2
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping({
            produces:[HttpMediaType.MULTIPART_FORM_DATA],
      })
    queryOrder_2!:(req?: OrderFeignClientQueryOrder_2Req, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:POST /order/queryPage
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：PageInfo
      * 5:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryPage!:(req?: OrderFeignClientQueryPageReq, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:GET /order/createOrder
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：Long
     **/
      @GetMapping({
      })
    createOrder!:(req: CreateOrderEvt, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:POST /order/hello
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：Object
     **/
      @PostMapping({
      })
    hello!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<any>;
    /**
      * 1:DELETE /order/delete
      * 2:Http请求方法：DELETE
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：Object
     **/
      @DeleteMapping({
            value:"/delete",
      })
    delete!:(req?: ExampleDTO, option?: FeignRequestOptions) => Promise<any>;
}

export default new OrderFeignClient();
