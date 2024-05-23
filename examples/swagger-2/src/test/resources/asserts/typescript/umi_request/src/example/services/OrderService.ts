/* tslint:disable */
  import request,{RequestOptionsInit} from 'umi-request';
      import {OrderServiceTestEnumNames3Req} from "../../req/OrderServiceTestEnumNames3Req";
      import {Sex} from "../../enums/Sex";
      import {OrderServiceHelloReq} from "../../req/OrderServiceHelloReq";
      import {Page} from "../../model/paging/Page";
      import {BaseServiceTest2Req} from "../../req/BaseServiceTest2Req";
      import {OrderServiceTestEnumNamesReq} from "../../req/OrderServiceTestEnumNamesReq";
      import {OrderServiceHello3Req} from "../../req/OrderServiceHello3Req";
      import {Order} from "../../domain/Order";
      import {OrderServiceTestEnumNames2Req} from "../../req/OrderServiceTestEnumNames2Req";
      import {CreateOrderEvt} from "../../evt/CreateOrderEvt";
      import {OrderServiceQueryPageReq} from "../../req/OrderServiceQueryPageReq";
      import {QueryOrderEvt} from "../../evt/QueryOrderEvt";
      import {OrderServiceQueryOrderReq} from "../../req/OrderServiceQueryOrderReq";
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
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Order
     **/

export const  getOrder=  (req: OrderServiceGetOrderReq, options?: RequestOptionsInit): Promise<Array<Order>> =>{
        const {names,...reqData} = req;
        const headers:Record<string,any>={};
            if(names!=null){
              headers['names']=Array.isArray(names)?names.join(";"):names;
            }
  return request<Array<Order>>(`/order/get_order`, {
      method: 'get',
        headers,
      params: reqData,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Order
     **/

export const  getOrder32=  (req: OrderServiceGetOrder32Req, options?: RequestOptionsInit): Promise<Array<Order>> =>{
  return request<Array<Order>>(`/order/get_order_32`, {
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/

export const  queryOrder=  (req: OrderServiceQueryOrderReq, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
        const {X-User-Id,...reqData} = req;
        const headers:Record<string,any>={};
            if(X-User-Id!=null){
              headers['X-User-Id']=Array.isArray(X-User-Id)?X-User-Id.join(";"):X-User-Id;
            }
  return request<PageInfo<Order>>(`/order/queryOrder`, {
      method: 'get',
        headers,
      params: reqData,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:获取订单列表
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Page
      * 4:返回值在java中的类型为：Order
     **/

export const  pageBySpringData=  (req: QueryOrderEvt, options?: RequestOptionsInit): Promise<Page<Order>> =>{
  return request<Page<Order>>(`/order`, {
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:获取订单列表
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：Order
     **/

export const  queryOrder2=  (req?: OrderServiceQueryOrder2Req, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order/queryOrder2`, {
      method: 'post',
          requestType: 'form',
          data: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

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

export const  queryPage=  (req?: OrderServiceQueryPageReq, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order/queryPage`, {
      method: 'post',
          requestType: 'json',
          data: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

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

export const  createOrder=  (req: CreateOrderEvt, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/order/createOrder`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:test hello
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceResponse
      * 4:返回值在java中的类型为：Object
     **/

export const  hello=  ( options?: RequestOptionsInit): Promise<any> =>{
  return request<any>(`/order/hello`, {
      method: 'post',
          requestType: 'form',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:test hello
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：Object
     **/

export const  hello2=  (req?: OrderServiceHello2Req, options?: RequestOptionsInit): Promise<PageInfo<any>> =>{
  return request<PageInfo<any>>(`/order/hello_2`, {
      method: 'post',
          requestType: 'form',
          data: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:test hello_3
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：ServiceQueryResponse
      * 4:返回值在java中的类型为：String
     **/

export const  hello3=  (req?: OrderServiceHello3Req, options?: RequestOptionsInit): Promise<PageInfo<string>> =>{
  return request<PageInfo<string>>(`/order/hello_3`, {
      method: 'post',
          requestType: 'form',
          data: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:test hello
      * 2:Http请求方法：DELETE
      * 3:返回值在java中的类型为：void
     **/

export const  deleteRequest=  (req?: OrderServiceDeleteReq, options?: RequestOptionsInit): Promise<void> =>{
  return request<void>(`/order/hello_delete`, {
      method: 'delete',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Sex
      * 4:返回值在java中的类型为：Sex
     **/

export const  testEnumNames=  ( options?: RequestOptionsInit): Promise<Record<'MAN' | 'WOMAN' | 'NONE','MAN' | 'WOMAN' | 'NONE'>> =>{
  return request<Record<'MAN' | 'WOMAN' | 'NONE','MAN' | 'WOMAN' | 'NONE'>>(`/order/testEnumNames`, {
      method: 'get',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：String
      * 4:返回值在java中的类型为：Sex
     **/

export const  testEnumNames2=  ( options?: RequestOptionsInit): Promise<Record<string,'MAN' | 'WOMAN' | 'NONE'>> =>{
  return request<Record<string,'MAN' | 'WOMAN' | 'NONE'>>(`/order/testEnumNames2`, {
      method: 'get',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：Map
      * 3:返回值在java中的类型为：Sex
      * 4:返回值在java中的类型为：Integer
     **/

export const  testEnumNames3=  ( options?: RequestOptionsInit): Promise<Record<'MAN' | 'WOMAN' | 'NONE',number>> =>{
  return request<Record<'MAN' | 'WOMAN' | 'NONE',number>>(`/order/testEnumNames3`, {
      method: 'get',
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：void
     **/

export const  test2=  (req?: BaseServiceTest2Req, options?: RequestOptionsInit): Promise<void> =>{
  return request<void>(`/test2`, {
      method: 'get',
      params: req || {},
      responseType: 'text',
  ...(options || {} as RequestOptionsInit)
  })
  }

