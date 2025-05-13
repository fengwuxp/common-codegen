/* tslint:disable */
import axios, {AxiosRequestConfig,AxiosResponse} from 'axios';
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
      * 1:GET /order/get_order
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Order
     **/

export const  getOrder=  (req: OrderServiceGetOrderReq, options?: AxiosRequestConfig): Promise<AxiosResponse<Array<Order>>> =>{
        const {names,...reqData} = req;
        const headers:Record<string,any>={};
            if(names!=null){
              headers['names']=Array.isArray(names)?names.join(";"):names;
            }
  return axios.request<Array<Order>>( {
      url:`/order/get_order`,
      method: 'get',
        headers,
      params: reqData,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /order/get_order_32
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：List
      * 5:返回值在java中的类型为：Order
     **/

export const  getOrder32=  (req: OrderServiceGetOrder32Req, options?: AxiosRequestConfig): Promise<AxiosResponse<Array<Order>>> =>{
  return axios.request<Array<Order>>( {
      url:`/order/get_order_32`,
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /order/queryOrder
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：PageInfo
      * 5:返回值在java中的类型为：Order
     **/

export const  queryOrder=  (req: OrderServiceQueryOrderReq, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
        const {X-User-Id,...reqData} = req;
        const headers:Record<string,any>={};
            if(X-User-Id!=null){
              headers['X-User-Id']=Array.isArray(X-User-Id)?X-User-Id.join(";"):X-User-Id;
            }
  return axios.request<PageInfo<Order>>( {
      url:`/order/queryOrder`,
      method: 'get',
        headers,
      params: reqData,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /order
      * 2:获取订单列表
      * 3:Http请求方法：GET
      * 4:返回值在java中的类型为：Page
      * 5:返回值在java中的类型为：Order
     **/

export const  pageBySpringData=  (req: QueryOrderEvt, options?: AxiosRequestConfig): Promise<AxiosResponse<Page<Order>>> =>{
  return axios.request<Page<Order>>( {
      url:`/order`,
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:POST /order/queryOrder2
      * 2:获取订单列表
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：Order
     **/

export const  queryOrder2=  (req?: OrderServiceQueryOrder2Req, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'multipart/form-data';
  return axios.request<PageInfo<Order>>( {
      url:`/order/queryOrder2`,
      method: 'post',
        headers,
          data: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

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

export const  queryPage=  (req?: OrderServiceQueryPageReq, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<PageInfo<Order>>( {
      url:`/order/queryPage`,
      method: 'post',
        headers,
          data: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

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

export const  createOrder=  (req: CreateOrderEvt, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<string>( {
      url:`/order/createOrder`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:POST /order/hello
      * 2:test hello
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceResponse
      * 5:返回值在java中的类型为：Object
     **/

export const  hello=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<any>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/x-www-form-urlencoded';
  return axios.request<any>( {
      url:`/order/hello`,
      method: 'post',
        headers,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:POST /order/hello_2
      * 2:test hello
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：Object
     **/

export const  hello2=  (req?: OrderServiceHello2Req, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<any>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/x-www-form-urlencoded';
  return axios.request<PageInfo<any>>( {
      url:`/order/hello_2`,
      method: 'post',
        headers,
          data: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:POST /order/hello_3
      * 2:test hello_3
      * 3:Http请求方法：POST
      * 4:返回值在java中的类型为：ServiceQueryResponse
      * 5:返回值在java中的类型为：String
     **/

export const  hello3=  (req?: OrderServiceHello3Req, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<string>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/x-www-form-urlencoded';
  return axios.request<PageInfo<string>>( {
      url:`/order/hello_3`,
      method: 'post',
        headers,
          data: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:DELETE /order/hello_delete
      * 2:test hello
      * 3:Http请求方法：DELETE
      * 4:返回值在java中的类型为：void
     **/

export const  deleteRequest=  (req?: OrderServiceDeleteReq, options?: AxiosRequestConfig): Promise<AxiosResponse<void>> =>{
  return axios.request<void>( {
      url:`/order/hello_delete`,
      method: 'delete',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /order/testEnumNames
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：Sex
     **/

export const  testEnumNames=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<Record<'MAN' | 'WOMAN' | 'NONE','MAN' | 'WOMAN' | 'NONE'>>> =>{
  return axios.request<Record<'MAN' | 'WOMAN' | 'NONE','MAN' | 'WOMAN' | 'NONE'>>( {
      url:`/order/testEnumNames`,
      method: 'get',
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /order/testEnumNames2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Sex
     **/

export const  testEnumNames2=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,'MAN' | 'WOMAN' | 'NONE'>>> =>{
  return axios.request<Record<string,'MAN' | 'WOMAN' | 'NONE'>>( {
      url:`/order/testEnumNames2`,
      method: 'get',
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /order/testEnumNames3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：Sex
      * 5:返回值在java中的类型为：Integer
     **/

export const  testEnumNames3=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<Record<'MAN' | 'WOMAN' | 'NONE',number>>> =>{
  return axios.request<Record<'MAN' | 'WOMAN' | 'NONE',number>>( {
      url:`/order/testEnumNames3`,
      method: 'get',
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：void
     **/

export const  test2=  (req?: BaseServiceTest2Req, options?: AxiosRequestConfig): Promise<AxiosResponse<void>> =>{
  return axios.request<void>( {
      url:`/test2`,
      method: 'get',
      params: req || {},
      responseType: 'text',
  ...(options || {} as AxiosRequestConfig)
  })
  }

