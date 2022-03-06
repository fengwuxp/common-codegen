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
      import {OrderServiceTestEnumNames2Req} from "../../req/OrderServiceTestEnumNames2Req";
      import {CreateOrderEvt} from "../../evt/CreateOrderEvt";
      import {OrderServiceQueryPageReq} from "../../req/OrderServiceQueryPageReq";
      import {QueryOrderEvt} from "../../evt/QueryOrderEvt";
      import {OrderServiceTestEnumNames3Req} from "../../req/OrderServiceTestEnumNames3Req";
      import {Sex} from "../../enums/Sex";
      import {OrderServiceHelloReq} from "../../req/OrderServiceHelloReq";
      import {OrderServiceGetOrder32Req} from "../../req/OrderServiceGetOrder32Req";
      import {OrderServiceDeleteReq} from "../../req/OrderServiceDeleteReq";
      import {BaseServiceTest2Req} from "../../req/BaseServiceTest2Req";
      import {PageInfo} from "../../resp/PageInfo";
      import {OrderServiceTestEnumNamesReq} from "../../req/OrderServiceTestEnumNamesReq";
      import {OrderServiceGetOrderReq} from "../../req/OrderServiceGetOrderReq";
      import {OrderServiceQueryOrder2Req} from "../../req/OrderServiceQueryOrder2Req";
      import {OrderServiceHello2Req} from "../../req/OrderServiceHello2Req";
      import {OrderServiceHello3Req} from "../../req/OrderServiceHello3Req";

    /**
     * 订单服务
     * 接口：GET
    **/
  @Feign({
        value:"/order",
  })
class OrderService{

    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Order
     **/
      @GetMapping({
            value:"get_order",
      })
    getOrder!:(req: OrderServiceGetOrderReq, option?: FeignRequestOptions) => Promise<Array<Order>>;
    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Order
     **/
      @GetMapping({
            value:"get_order_32",
      })
    getOrder32!:(req: OrderServiceGetOrder32Req, option?: FeignRequestOptions) => Promise<Array<Order>>;
    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/
      @GetMapping({
            value:"/queryOrder",
      })
    queryOrder!:(req: QueryOrderEvt, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Page
      * 4:返回值在java中的类型为：Order
     **/
      @GetMapping({
      })
    pageBySpringData!:(req: QueryOrderEvt, option?: FeignRequestOptions) => Promise<Order>;
    /**
      * 1:获取订单列表
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：Order
     **/
      @PostMapping({
            produces:[HttpMediaType.MULTIPART_FORM_DATA],
      })
    queryOrder2!:(req?: OrderServiceQueryOrder2Req, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:查询分页
      * 2:Http请求方法：POST
      * <pre>
      * 4:参数列表：
      * 5:参数名称：id，参数说明：null
      * </pre>
      * 7:返回值在java中的类型为：ServiceResponse
      * 8:返回值在java中的类型为：PageInfo
      * 9:返回值在java中的类型为：Order
     **/
      @PostMapping({
      })
    queryPage!:(req?: OrderServiceQueryPageReq, option?: FeignRequestOptions) => Promise<PageInfo<Order>>;
    /**
      * 1:创建订单
      * 2:Http请求方法：POST
      * <pre>
      * 4:参数列表：
      * 5:参数名称：evt，参数说明：null
      * </pre>
      * 7:返回值在java中的类型为：ServiceResponse
      * 8:返回值在java中的类型为：Long
     **/
      @PostMapping({
      })
    createOrder!:(req: CreateOrderEvt, option?: FeignRequestOptions) => Promise<string>;
    /**
      * 1:test hello
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：Object
     **/
      @PostMapping({
      })
    hello!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<any>;
    /**
      * 1:test hello
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：Object
     **/
      @PostMapping({
            value:"hello_2",
      })
    hello2!:(req?: OrderServiceHello2Req, option?: FeignRequestOptions) => Promise<PageInfo<any>>;
    /**
      * 1:test hello_3
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：String
     **/
      @PostMapping({
            value:"hello_3",
      })
    hello3!:(req?: OrderServiceHello3Req, option?: FeignRequestOptions) => Promise<PageInfo<string>>;
    /**
      * 1:test hello
      * 2:Http请求方法：DELETE
      * 3:返回值在java中的类型为：void
     **/
      @DeleteMapping({
            value:"hello_delete",
      })
    delete!:(req?: OrderServiceDeleteReq, option?: FeignRequestOptions) => Promise<void>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Sex
      * 4:返回值在java中的类型为：Sex
     **/
      @GetMapping({
            value:"/testEnumNames",
      })
    testEnumNames!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Record<'MAN' | 'WOMAN' | 'NONE','MAN' | 'WOMAN' | 'NONE'>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：Sex
     **/
      @GetMapping({
            value:"/testEnumNames2",
      })
    testEnumNames2!:(req?: null | undefined, option?: FeignRequestOptions) => Promise<Record<string,'MAN' | 'WOMAN' | 'NONE'>>;
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Sex
      * 4:返回值在java中的类型为：Integer
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
