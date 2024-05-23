/* tslint:disable */
  import request,{RequestOptionsInit} from 'umi-request';
      import {Order} from "../../domain/Order";
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
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：List
      * 3:返回值在java中的类型为：Order
     **/

export const  getOrder=  (req: OrderFeignClientGetOrderReq, options?: RequestOptionsInit): Promise<Array<Order>> =>{
        const {My-Ids,...reqData} = req;
        const headers:Record<string,any>={};
            if(My-Ids!=null){
              headers['My-Ids']=Array.isArray(My-Ids)?My-Ids.join(";"):My-Ids;
            }
  return request<Array<Order>>(`/order/getOrder`, {
      method: 'get',
        headers,
      params: reqData,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder2=  (req?: QueryOrderEvt, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order`, {
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder=  (req?: QueryOrderEvt, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order`, {
      method: 'post',
          requestType: 'json',
          data: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder3=  (req: QueryOrderEvt[], options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order/queryOrder3`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder4=  (req: Array<QueryOrderEvt>, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order/queryOrder4`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder5=  (req: Record<string,QueryOrderEvt>, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order/queryOrder5`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder6=  (req: OrderFeignClientQueryOrder6Req, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order/queryOrder6`, {
      method: 'post',
          requestType: 'json',
          data: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceQueryResponse
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder_2=  (req?: OrderFeignClientQueryOrder_2Req, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order/queryOrder_2`, {
      method: 'post',
          requestType: 'form',
          data: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
     **/

export const  queryPage=  (req?: OrderFeignClientQueryPageReq, options?: RequestOptionsInit): Promise<PageInfo<Order>> =>{
  return request<PageInfo<Order>>(`/order/queryPage`, {
      method: 'post',
          requestType: 'json',
          data: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Long
     **/

export const  createOrder=  (req: CreateOrderEvt, options?: RequestOptionsInit): Promise<string> =>{
  return request<string>(`/order/createOrder`, {
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Object
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
      * 1:Http请求方法：DELETE
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Object
     **/

export const  deleteRequest=  (req?: ExampleDTO, options?: RequestOptionsInit): Promise<any> =>{
  return request<any>(`/order/delete`, {
      method: 'delete',
      params: req || {},
      responseType: 'json',
  ...(options || {} as RequestOptionsInit)
  })
  }

