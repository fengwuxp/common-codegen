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
        import {OrderServiceGetOrder32Req} from "../../req/OrderServiceGetOrder32Req";
        import {OrderServiceDeleteReq} from "../../req/OrderServiceDeleteReq";
        import {PageInfo} from "../../resp/PageInfo";
        import {OrderServiceGetOrderReq} from "../../req/OrderServiceGetOrderReq";
        import {OrderServiceQueryOrder2Req} from "../../req/OrderServiceQueryOrder2Req";
        import {OrderServiceHello2Req} from "../../req/OrderServiceHello2Req";

    /**
     * 订单服务
     * 接口：GET
    **/
        const API_FUNCTION_FACTORY = feignHttpFunctionBuilder({
            value:"/order",
        });
    /**
      * 1:GET /order/get_order
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Order
     **/
    export const getOrder: FeignHttpClientPromiseFunction<OrderServiceGetOrderReq ,Array<Order>> = API_FUNCTION_FACTORY.get({
                value:"get_order",
                headers:{"names":"{names}"},
    });
    /**
      * 1:GET /order/get_order_32
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Order
     **/
    export const getOrder32: FeignHttpClientPromiseFunction<OrderServiceGetOrder32Req ,Array<Order>> = API_FUNCTION_FACTORY.get({
                value:"get_order_32",
    });
    /**
      * 1:GET /order/queryOrder
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：PageInfo
      * 5:返回值在java中的类型为：Order
     **/
    export const queryOrder: FeignHttpClientPromiseFunction<OrderServiceQueryOrderReq ,PageInfo<Order>> = API_FUNCTION_FACTORY.get({
                value:"/queryOrder",
                headers:{"X-User-Id":"{userId}"},
                queryArgNames:["evt"],
    });
    /**
      * 1:GET /order
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Page
      * 5:返回值在java中的类型为：Order
     **/
    export const pageBySpringData: FeignHttpClientPromiseFunction<QueryOrderEvt ,Page<Order>> = API_FUNCTION_FACTORY.get({
    });
    /**
      * 1:POST /order/queryOrder2
      * 2:获取订单列表
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：Order
     **/
    export const queryOrder2: FeignHttpClientPromiseFunction<OrderServiceQueryOrder2Req |void,PageInfo<Order>> = API_FUNCTION_FACTORY.post({
                produces:[HttpMediaType.MULTIPART_FORM_DATA],
    });
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
    export const queryPage: FeignHttpClientPromiseFunction<OrderServiceQueryPageReq |void,PageInfo<Order>> = API_FUNCTION_FACTORY.post({
    });
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
    export const createOrder: FeignHttpClientPromiseFunction<CreateOrderEvt ,string> = API_FUNCTION_FACTORY.post({
    });
    /**
      * 1:POST /order/hello
      * 2:test hello
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceResponse
      * 5:返回值在java中的类型为：Object
     **/
    export const hello: FeignHttpClientPromiseFunction<void,any> = API_FUNCTION_FACTORY.post({
    });
    /**
      * 1:POST /order/hello_2
      * 2:test hello
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：Object
     **/
    export const hello2: FeignHttpClientPromiseFunction<OrderServiceHello2Req |void,PageInfo<any>> = API_FUNCTION_FACTORY.post({
                value:"hello_2",
    });
    /**
      * 1:POST /order/hello_3
      * 2:test hello_3
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：String
     **/
    export const hello3: FeignHttpClientPromiseFunction<OrderServiceHello3Req |void,PageInfo<string>> = API_FUNCTION_FACTORY.post({
                value:"hello_3",
    });
    /**
      * 1:DELETE /order/hello_delete
      * 2:test hello
      * 3:Http请求方法：DELETE
      * 4:返回值在java中的类型为：void
     **/
    export const delete_request: FeignHttpClientPromiseFunction<OrderServiceDeleteReq |void,void> = API_FUNCTION_FACTORY.delete({
                value:"hello_delete",
    });
    /**
      * 1:GET /order/testEnumNames
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：Sex
     **/
    export const testEnumNames: FeignHttpClientPromiseFunction<void,Record<'MAN' | 'WOMAN' | 'NONE','MAN' | 'WOMAN' | 'NONE'>> = API_FUNCTION_FACTORY.get({
                value:"/testEnumNames",
    });
    /**
      * 1:GET /order/testEnumNames2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Sex
     **/
    export const testEnumNames2: FeignHttpClientPromiseFunction<void,Record<string,'MAN' | 'WOMAN' | 'NONE'>> = API_FUNCTION_FACTORY.get({
                value:"/testEnumNames2",
    });
    /**
      * 1:GET /order/testEnumNames3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：Integer
     **/
    export const testEnumNames3: FeignHttpClientPromiseFunction<void,Record<'MAN' | 'WOMAN' | 'NONE',number>> = API_FUNCTION_FACTORY.get({
                value:"/testEnumNames3",
    });
    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：void
     **/
    export const test2: FeignHttpClientPromiseFunction<BaseServiceTest2Req |void,void> = API_FUNCTION_FACTORY.get({
                value:"/test2",
    });
