/* tslint:disable */
import axios, {AxiosRequestConfig,AxiosResponse} from 'axios';
      import {Order} from "../../domain/Order";
      import {User} from "../../domain/User";

    /**
     * list tst
     * 接口：GET
     * list test（源码注释）
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:POST /list
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/

export const  test1=  (req: Array<User>, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<string>( {
      url:`/list`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /list/test_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/

export const  test2=  (req: User[], options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
  return axios.request<string>( {
      url:`/list/test_2`,
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /list/test_3
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：String
     **/

export const  test3=  (req: Record<string,Order>, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
  return axios.request<string>( {
      url:`/list/test_3`,
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:POST /list/test_4
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/

export const  test4=  (req: Array<User>, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<string>( {
      url:`/list/test_4`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:POST /list/test_5
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/

export const  test5=  (req: Array<User>, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<string>( {
      url:`/list/test_5`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:POST /list/test_6
      * 2:Http请求方法：POST
      * 3:返回值在java中的类型为：String
     **/

export const  test6=  (req: Array<User>, options?: AxiosRequestConfig): Promise<AxiosResponse<string>> =>{
        const headers:Record<string,any>={};
                headers['Content-Type']= 'application/json';
  return axios.request<string>( {
      url:`/list/test_6`,
      method: 'post',
        headers,
          data: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

