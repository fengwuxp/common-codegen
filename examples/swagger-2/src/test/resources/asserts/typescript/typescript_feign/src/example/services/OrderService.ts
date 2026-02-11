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
      import {OrderServiceTestEnumNames3Req} from "../../req/OrderServiceTestEnumNames3Req";
      import {OrderServiceHelloReq} from "../../req/OrderServiceHelloReq";
      import {Sex} from "../../enums/Sex";
      import {Page} from "../../model/paging/Page";
      import {BaseServiceTest2Req} from "../../req/BaseServiceTest2Req";
      import {OrderServiceTestEnumNamesReq} from "../../req/OrderServiceTestEnumNamesReq";
      import {OrderServiceHello3Req} from "../../req/OrderServiceHello3Req";
      import {Order} from "../../domain/Order";
      import {OrderServiceQueryOrderReq} from "../../req/OrderServiceQueryOrderReq";
      import {QueryOrderEvt} from "../../evt/QueryOrderEvt";
      import {OrderServiceQueryPageReq} from "../../req/OrderServiceQueryPageReq";
      import {CreateOrderEvt} from "../../evt/CreateOrderEvt";
      import {OrderServiceTestEnumNames2Req} from "../../req/OrderServiceTestEnumNames2Req";
      import {OrderServiceDeleteReq} from "../../req/OrderServiceDeleteReq";
      import {PageInfo} from "../../resp/PageInfo";
      import {OrderServiceGetOrderReq} from "../../req/OrderServiceGetOrderReq";
      import {OrderServiceQueryOrder2Req} from "../../req/OrderServiceQueryOrder2Req";
      import {OrderServiceHello2Req} from "../../req/OrderServiceHello2Req";

    /**
     * 订单服务
     * 接口：GET
    **/
  @Feign({
        value:"/order",
  })
class OrderService{

    /**
      * 1:GET /order/get_order
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Order
     **/
      @GetMapping({
            value:"get_order",
            headers:{"names":"{names}"},
      })
    getOrder!:(req: OrderServiceGetOrderReq, option?: FeignRequestOptions) => Promise<Array<Order>>;
    /**
      * 1:GET /order/get_order_32
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Order
     **/
      @GetMapping({
            value:"get_order_32",
      })
    getOrder32!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Array<Order>>;
    /**
      * 1:GET /order/queryOrder
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：PageInfo
      * 5:返回值在java中的类型为：Order
     **/
      @GetMapping({
            value:"/queryOrder",
            headers:{"X-User-Id":"{userId}"},
            queryArgNames:["evt"],
      })
    queryOrder!:(req: OrderServiceQueryOrderReq, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:GET /order
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Page
      * 5:返回值在java中的类型为：Order
     **/
      @GetMapping({
      })
    pageBySpringData!:(req: QueryOrderEvt, option?: FeignRequestOptions) => Promise<Page<Order>>;
    /**
      * 1:POST /order/queryOrder2
      * 2:获取订单列表
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：Order
     **/
      @PostMapping({
            produces:[HttpMediaType.MULTIPART_FORM_DATA],
      })
    queryOrder2!:(req?: OrderServiceQueryOrder2Req, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:POST /order/queryPage
      * 2:查询分页
      * 3:Http请求方法：POST
      * <pre>
      * 5:参数列表：
      * 6:参数名称：id，参数说明：null
      * </pre>
      * 8:返回值在java中的类型为：ServiceResponse
      * 9:返回值在java中的类型为：PageInfo
      * 10:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryPage!:(req?: OrderServiceQueryPageReq, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:POST /order/createOrder
      * 2:创建订单
      * 3:Http请求方法：POST
      * <pre>
      * 5:参数列表：
      * 6:参数名称：evt，参数说明：null
      * </pre>
      * 8:返回值在java中的类型为：ServiceResponse
      * 9:返回值在java中的类型为：Long
     **/
      @PostMapping({
      })
    createOrder!:(req: CreateOrderEvt, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:POST /order/hello
      * 2:test hello
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceResponse
      * 5:返回值在java中的类型为：Object
     **/
      @PostMapping({
      })
    hello!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<any>;
    /**
      * 1:POST /order/hello_2
      * 2:test hello
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：Object
     **/
      @PostMapping({
            value:"hello_2",
      })
    hello2!:(req?: OrderServiceHello2Req, option?: FeignRequestOptions) => Promise<PageInfo<any>>;
    /**
      * 1:POST /order/hello_3
      * 2:test hello_3
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：String
     **/
      @PostMapping({
            value:"hello_3",
      })
    hello3!:(req?: OrderServiceHello3Req, option?: FeignRequestOptions) => Promise<PageInfo<string>>;
    /**
      * 1:DELETE /order/hello_delete
      * 2:test hello
      * 3:Http请求方法：DELETE
      * 4:返回值在java中的类型为：void
     **/
      @DeleteMapping({
            value:"hello_delete",
      })
    delete!:(req?: OrderServiceDeleteReq, option?: FeignRequestOptions) => Promise<void>;
    /**
      * 1:GET /order/testEnumNames
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：Sex
     **/
      @GetMapping({
            value:"/testEnumNames",
      })
    testEnumNames!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Record<'MAN' | 'WOMAN' | 'NONE','MAN' | 'WOMAN' | 'NONE'>>;
    /**
      * 1:GET /order/testEnumNames2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Sex
     **/
      @GetMapping({
            value:"/testEnumNames2",
      })
    testEnumNames2!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Record<string,'MAN' | 'WOMAN' | 'NONE'>>;
    /**
      * 1:GET /order/testEnumNames3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：Integer
     **/
      @GetMapping({
            value:"/testEnumNames3",
      })
    testEnumNames3!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Record<'MAN' | 'WOMAN' | 'NONE',number>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：void
     **/
      @GetMapping({
            value:"/test2",
      })
    test2!:(req?: BaseServiceTest2Req, option?: FeignRequestOptions) => Promise<void>;
}

export default new OrderService();
