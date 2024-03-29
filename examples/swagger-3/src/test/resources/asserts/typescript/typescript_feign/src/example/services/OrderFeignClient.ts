/* tslint:disable */
/* eslint-disable */

import {
Feign,
RequestMapping,
PostMapping,
DeleteMapping,
GetMapping,
PutMapping,
Signature,
HttpMediaType,
AuthenticationType,
FeignRequestOptions} from "fengwuxp-typescript-feign";
      import {Order} from "../../domain/Order";
      import {User} from "../../domain/User";
      import {CreateOrderEvt} from "../../evt/CreateOrderEvt";
      import {QueryOrderEvt} from "../../evt/QueryOrderEvt";
      import {OrderFeignClientQueryPageReq} from "../../req/OrderFeignClientQueryPageReq";
      import {ExampleDTO} from "../../evt/ExampleDTO";
      import {OrderFeignClientHelloReq} from "../../req/OrderFeignClientHelloReq";
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
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：List
      * 3:返回值在java中的类型为：Order
     **/
      @GetMapping({
      })
    getOrder!:(req: OrderFeignClientGetOrderReq, option?: FeignRequestOptions) => Promise<Array<Order>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @GetMapping({
      })
    queryOrder2!:(req?: QueryOrderEvt, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder!:(req?: QueryOrderEvt, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder3!:(req: QueryOrderEvt[], option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder4!:(req: Array<QueryOrderEvt>, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder5!:(req: Record<string,QueryOrderEvt>, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryOrder6!:(req: OrderFeignClientQueryOrder6Req, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceQueryResponse
      * 3:返回值在java中的类型为：Order
     **/
      @PostMapping({
            produces:[HttpMediaType.MULTIPART_FORM_DATA],
      })
    queryOrder_2!:(req?: OrderFeignClientQueryOrder_2Req, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryPage!:(req?: OrderFeignClientQueryPageReq, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Long
     **/
      @GetMapping({
      })
    createOrder!:(req: CreateOrderEvt, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Object
     **/
      @PostMapping({
      })
    hello!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<any>;
    /**
      * 1:Http请求方法：DELETE
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Object
     **/
      @DeleteMapping({
            value:"/delete",
      })
    delete!:(req?: ExampleDTO, option?: FeignRequestOptions) => Promise<any>;
}

export default new OrderFeignClient();
