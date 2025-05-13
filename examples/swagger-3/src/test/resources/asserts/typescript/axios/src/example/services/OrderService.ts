/* tslint:disable */
import axios, {AxiosRequestConfig,AxiosResponse} from 'axios';
      import {Order} from "../../domain/Order";
      import {User} from "../../domain/User";
      import {CreateOrderEvt} from "../../evt/CreateOrderEvt";
      import {OrderServiceQueryPageReq} from "../../req/OrderServiceQueryPageReq";
      import {QueryOrderEvt} from "../../evt/QueryOrderEvt";
      import {OrderServiceHelloReq} from "../../req/OrderServiceHelloReq";
      import {OrderServiceQueryOrder_2Req} from "../../req/OrderServiceQueryOrder_2Req";
      import {ExampleDTO} from "../../evt/ExampleDTO";
      import {PageInfo} from "../../resp/PageInfo";
      import {OrderServiceGetOrderReq} from "../../req/OrderServiceGetOrderReq";
      import {OrderServiceQueryOrder6Req} from "../../req/OrderServiceQueryOrder6Req";

    /**
     * 接口：GET
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：List
      * 3:返回值在java中的类型为：Order
     **/

export const  getOrder=  (req: OrderServiceGetOrderReq, options?: AxiosRequestConfig): Promise<AxiosResponse<Array<Order>>> =>{
        const {ids,...reqData} = req;
        const headers:Record<string,any>={};
            if(ids!=null){
              headers['ids']=Array.isArray(ids)?ids.join(";"):ids;
            }
  return axios.request<Array<Order>>( {
      url:`/order/getOrder`,
      method: 'get',
        headers,
      params: reqData,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder2=  (req?: QueryOrderEvt, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
  return axios.request<PageInfo<Order>>( {
      url:`/order`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder=  (req?: QueryOrderEvt, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<PageInfo<Order>>( {
      url:`/order`,
      method: 'post',
        headers,
          data: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder3=  (req: QueryOrderEvt[], options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<PageInfo<Order>>( {
      url:`/order/queryOrder3`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder4=  (req: Array<QueryOrderEvt>, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<PageInfo<Order>>( {
      url:`/order/queryOrder4`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder5=  (req: Record<string,QueryOrderEvt>, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<PageInfo<Order>>( {
      url:`/order/queryOrder5`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：PageInfo
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder6=  (req: OrderServiceQueryOrder6Req, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<PageInfo<Order>>( {
      url:`/order/queryOrder6`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceQueryResponse
      * 3:返回值在java中的类型为：Order
     **/

export const  queryOrder_2=  (req?: OrderServiceQueryOrder_2Req, options?: AxiosRequestConfig): Promise<AxiosResponse<PageInfo<Order>>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'multipart/form-data';
  return axios.request<PageInfo<Order>>( {
      url:`/order/queryOrder_2`,
      method: 'post',
        headers,
          data: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：PageInfo
      * 4:返回值在java中的类型为：Order
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
      * 1:Http请求方法：GET
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Long
     **/

export const  createOrder=  (req: CreateOrderEvt, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
  return axios.request<string>( {
      url:`/order/createOrder`,
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:Http请求方法：POST
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Object
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
      * 1:Http请求方法：DELETE
      * 2:返回值在java中的类型为：ServiceResponse
      * 3:返回值在java中的类型为：Object
     **/

export const  deleteRequest=  (req?: ExampleDTO, options?: AxiosRequestConfig): Promise<AxiosResponse<any>> =>{
  return axios.request<any>( {
      url:`/order/delete`,
      method: 'delete',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

