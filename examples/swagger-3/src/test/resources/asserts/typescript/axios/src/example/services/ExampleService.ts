/* tslint:disable */
import axios, {AxiosRequestConfig,AxiosResponse} from 'axios';
      import {ExampleServiceGetMap3Req} from "../../req/ExampleServiceGetMap3Req";
      import {ExampleServiceGetMap2Req} from "../../req/ExampleServiceGetMap2Req";
      import {User} from "../../domain/User";
      import {BaseInfo} from "../../domain/BaseInfo";
      import {ExampleServiceGetMapsReq} from "../../req/ExampleServiceGetMapsReq";
      import {ExampleServiceGetMapReq} from "../../req/ExampleServiceGetMapReq";
      import {ExampleDTO} from "../../resp/ExampleDTO";
      import {ExampleServiceGetNumsReq} from "../../req/ExampleServiceGetNumsReq";

    /**
     * 接口：GET
     * example_cms
    **/
/*================================================分割线，以下为接口列表===================================================*/

    /**
      * 1:GET /example_cms/get_num
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Integer
     **/

export const  getNums=  (req: ExampleServiceGetNumsReq, options?: AxiosRequestConfig): Promise<AxiosResponse<Array<number>>> =>{
  return axios.request<Array<number>>( {
      url:`/example_cms/get_num`,
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /example_cms/get_maps
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：List
      * 4:返回值在java中的类型为：Map
      * 5:返回值在java中的类型为：Integer
      * 6:返回值在java中的类型为：String
     **/

export const  getMaps=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<Array<Record<number,string>>>> =>{
  return axios.request<Array<Record<number,string>>>( {
      url:`/example_cms/get_maps`,
      method: 'get',
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /example_cms/get_map
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：Integer
     **/

export const  getMap=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,number>>> =>{
  return axios.request<Record<string,number>>( {
      url:`/example_cms/get_map`,
      method: 'get',
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /example_cms/get_map_2
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/

export const  getMap2=  ( options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,Array<boolean>>>> =>{
  return axios.request<Record<string,Array<boolean>>>( {
      url:`/example_cms/get_map_2`,
      method: 'get',
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /example_cms/get_map_3/{id}
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/

export const  getMap3=  (req: ExampleServiceGetMap3Req, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,Array<boolean>>>> =>{
        const {id,...reqData} = req;
        const url =`/example_cms/get_map_3/${id}`;
  return axios.request<Record<string,Array<boolean>>>( {
      url:url,
      method: 'get',
      params: reqData,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /example_cms/get_map_/
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/

export const  getMap4=  (req?: ExampleDTO, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,Array<boolean>>>> =>{
  return axios.request<Record<string,Array<boolean>>>( {
      url:`/example_cms/get_map_/`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /example_cms/get_map_5
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：Map
      * 4:返回值在java中的类型为：String
      * 5:返回值在java中的类型为：List
      * 6:返回值在java中的类型为：Boolean
     **/

export const  getMap5=  (req?: ExampleDTO, options?: AxiosRequestConfig): Promise<AxiosResponse<Record<string,Array<boolean>>>> =>{
  return axios.request<Record<string,Array<boolean>>>( {
      url:`/example_cms/get_map_5`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /example_cms/get_map_6
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/

export const  example0=  (req?: BaseInfo<string,User>, options?: AxiosRequestConfig): Promise<AxiosResponse<void>> =>{
  return axios.request<void>( {
      url:`/example_cms/get_map_6`,
      method: 'get',
      params: req || {},
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /example_cms/get_map_7
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/

export const  example2=  (req: Record<string,User>, options?: AxiosRequestConfig): Promise<AxiosResponse<void>> =>{
  return axios.request<void>( {
      url:`/example_cms/get_map_7`,
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

    /**
      * 1:GET /example_cms/get_map_8
      * 2:Http请求方法：GET
      * 3:返回值在java中的类型为：void
     **/

export const  example3=  (req: Array<User>, options?: AxiosRequestConfig): Promise<AxiosResponse<void>> =>{
  return axios.request<void>( {
      url:`/example_cms/get_map_8`,
      method: 'get',
      params: req,
      responseType: 'json',
  ...(options || {} as AxiosRequestConfig)
  })
  }

